package com.jalrakshak.jalrakshak.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalrakshak.jalrakshak.dto.AiAssistantRequest;
import com.jalrakshak.jalrakshak.dto.AiAssistantResponse;
import com.jalrakshak.jalrakshak.dto.ComplaintClassificationResult;
import com.jalrakshak.jalrakshak.model.Alert;
import com.jalrakshak.jalrakshak.model.Complaint;
import com.jalrakshak.jalrakshak.model.HealthCaseReport;
import com.jalrakshak.jalrakshak.model.Village;
import com.jalrakshak.jalrakshak.model.WaterQualityReport;
import com.jalrakshak.jalrakshak.repository.AlertRepository;
import com.jalrakshak.jalrakshak.repository.ComplaintRepository;
import com.jalrakshak.jalrakshak.repository.HealthCaseRepository;
import com.jalrakshak.jalrakshak.repository.WaterQualityRepository;

import reactor.core.publisher.Flux;

@Service
public class AiAssistantService {

	private static final Logger log = LoggerFactory.getLogger(AiAssistantService.class);

	private static final int MAX_RETRIES = 2;
	private static final long RISK_CACHE_TTL_MS = TimeUnit.MINUTES.toMillis(10);
	private static final int MAX_HISTORY_MESSAGES = 12; // includes the system message

	private final ChatClient chatClient;
	private final VillageService villageService;
	private final ComplaintRepository complaintRepository;
	private final HealthCaseRepository healthCaseRepository;
	private final WaterQualityRepository waterQualityRepository;
	private final AlertRepository alertRepository;
	private final ObjectMapper objectMapper = new ObjectMapper();

	// In-memory conversation memory, keyed by sessionId.
	// NOTE: for multi-instance deployments, replace with a shared store (Redis, DB,
	// etc).
	private final Map<String, List<Message>> conversationStore = new ConcurrentHashMap<>();

	// In-memory cache for village risk summaries to avoid redundant AI calls.
	private final Map<Long, CacheEntry> riskSummaryCache = new ConcurrentHashMap<>();

	public AiAssistantService(ChatClient.Builder chatClientBuilder, VillageService villageService,
			ComplaintRepository complaintRepository, HealthCaseRepository healthCaseRepository,
			WaterQualityRepository waterQualityRepository, AlertRepository alertRepository) {

		this.chatClient = chatClientBuilder.build();
		this.villageService = villageService;
		this.complaintRepository = complaintRepository;
		this.healthCaseRepository = healthCaseRepository;
		this.waterQualityRepository = waterQualityRepository;
		this.alertRepository = alertRepository;
	}

	// =========================================================================================
	// CORE (ORIGINAL) FEATURES — now backed by retry-hardened callAi()
	// =========================================================================================

	public AiAssistantResponse askAssistant(AiAssistantRequest request) {

		String systemPrompt = buildSystemPrompt(request.getRole(), request.getLanguage());

		String userPrompt = """
				User Question:
				%s

				Answer according to the user's role.
				""".formatted(safe(request.getQuestion()));

		String answer = callAi(systemPrompt, userPrompt);

		return new AiAssistantResponse(answer);
	}

	/**
	 * Async variant so callers (e.g. controllers) can fire this off without
	 * blocking a request thread.
	 */
	public CompletableFuture<AiAssistantResponse> askAssistantAsync(AiAssistantRequest request) {
		return CompletableFuture.supplyAsync(() -> askAssistant(request));
	}

	/**
	 * Streaming variant for real-time typing-effect UIs. Requires reactor-core /
	 * WebFlux on the classpath (already a transitive dependency of spring-ai's
	 * reactive ChatClient support).
	 */
	public Flux<String> streamAssistant(AiAssistantRequest request) {

		String systemPrompt = buildSystemPrompt(request.getRole(), request.getLanguage());
		String userPrompt = """
				User Question:
				%s

				Answer according to the user's role.
				""".formatted(safe(request.getQuestion()));

		try {
			return chatClient.prompt().system(systemPrompt).user(userPrompt).stream().content().onErrorResume(e -> {
				log.error("Streaming AI call failed", e);
				return Flux.just("AI service is currently not available. Error: " + e.getMessage());
			});
		} catch (Exception e) {
			log.error("Failed to start AI stream", e);
			return Flux.just("AI service is currently not available. Error: " + e.getMessage());
		}
	}

	public AiAssistantResponse villageRiskSummary(Long villageId, String role, String language) {
		return villageRiskSummary(villageId, role, language, false);
	}

	/**
	 * @param forceRefresh bypass the cache and recompute even if a fresh cached
	 *                     value exists.
	 */
	public AiAssistantResponse villageRiskSummary(Long villageId, String role, String language, boolean forceRefresh) {

		requireVillageId(villageId);

		if (!forceRefresh) {
			CacheEntry cached = riskSummaryCache.get(villageId);
			if (cached != null && !cached.isExpired()) {
				log.debug("Serving cached risk summary for village {}", villageId);
				return new AiAssistantResponse(cached.value());
			}
		}

		String systemPrompt = buildSystemPrompt(role, language);
		String villageData = buildVillageData(villageId);

		String userPrompt = """
				Analyze the following village data.

				Give answer in this format:
				1. Current Risk Level
				2. Main Reasons
				3. Advice for Citizens
				4. Action for Officers
				5. Priority: LOW / MEDIUM / HIGH

				Use only given data. Do not invent fake data.

				Village Data:
				%s
				""".formatted(villageData);

		String answer = callAi(systemPrompt, userPrompt);

		riskSummaryCache.put(villageId, new CacheEntry(answer, System.currentTimeMillis()));

		return new AiAssistantResponse(answer);
	}

	public void invalidateRiskCache(Long villageId) {
		riskSummaryCache.remove(villageId);
	}

	public AiAssistantResponse healthAdvice(AiAssistantRequest request) {

		String systemPrompt = buildSystemPrompt(request.getRole(), request.getLanguage());

		String villageData = "";

		if (request.getVillageId() != null) {
			villageData = buildVillageData(request.getVillageId());
		}

		String userPrompt = """
				Give health advice for the following issue.

				Symptoms:
				%s

				Age Group:
				%s

				Case Count:
				%s

				Village Data:
				%s

				Important:
				- Give simple first-aid and safety advice.
				- Do not give dangerous medicine advice.
				- Tell when to contact ASHA worker / doctor / health center.
				- Mention danger signs like dehydration, blood in stool, high fever, repeated vomiting.
				""".formatted(safe(request.getSymptoms()), safe(request.getAgeGroup()),
				request.getCaseCount() == null ? "Not provided" : request.getCaseCount(), villageData);

		String answer = callAi(systemPrompt, userPrompt);

		return new AiAssistantResponse(answer);
	}

	public AiAssistantResponse officerActionPlan(Long villageId, String role, String language) {

		requireVillageId(villageId);

		String systemPrompt = buildSystemPrompt(role, language);
		String villageData = buildVillageData(villageId);

		String userPrompt = """
				You are helping a JalRakshak officer.

				Based on the village data, create an officer action plan.

				Give answer in this format:
				1. Immediate Action
				2. Water Testing Action
				3. Health Team Action
				4. Panchayat Action
				5. Citizen Communication Message
				6. Follow-up Plan

				Village Data:
				%s
				""".formatted(villageData);

		String answer = callAi(systemPrompt, userPrompt);

		return new AiAssistantResponse(answer);
	}

	public AiAssistantResponse classifyComplaint(AiAssistantRequest request) {

		String systemPrompt = buildSystemPrompt(request.getRole(), request.getLanguage());

		String userPrompt = """
				Classify this citizen complaint.

				Complaint Description:
				%s

				Water Source:
				%s

				Select the best complaint type from:
				DIRTY_WATER,
				BAD_SMELL,
				PIPELINE_LEAKAGE,
				NO_WATER_SUPPLY,
				HANDPUMP_NOT_WORKING,
				TANK_CLEANING_ISSUE,
				OTHER

				Give answer in this exact format:
				Complaint Type:
				Urgency: LOW / MEDIUM / HIGH
				Reason:
				Suggested Department:
				Citizen Advice:
				""".formatted(safe(request.getComplaintDescription()), safe(request.getWaterSource()));

		String answer = callAi(systemPrompt, userPrompt);

		return new AiAssistantResponse(answer);
	}

	/**
	 * Structured version of classifyComplaint(): combines a fast rule-based urgency
	 * hint (keyword scan) with the AI's own judgement, and returns clean JSON that
	 * a frontend can bind directly to UI fields instead of parsing free text.
	 */
	public AiAssistantResponse classifyComplaintStructured(AiAssistantRequest request) {

		String systemPrompt = buildSystemPrompt(request.getRole(), request.getLanguage());
		String ruleHint = ruleBasedUrgencyHint(request.getComplaintDescription());

		String userPrompt = """
				Classify this citizen complaint and respond ONLY with valid JSON matching the schema below.
				No markdown, no code fences, no explanation outside the JSON object.

				Complaint Description:
				%s

				Water Source:
				%s

				Rule-based urgency hint (consider it, but use your own judgement): %s

				complaintType must be one of:
				DIRTY_WATER, BAD_SMELL, PIPELINE_LEAKAGE, NO_WATER_SUPPLY, HANDPUMP_NOT_WORKING, TANK_CLEANING_ISSUE, OTHER

				JSON schema:
				{
				  "complaintType": "string",
				  "urgency": "LOW|MEDIUM|HIGH",
				  "reason": "string",
				  "suggestedDepartment": "string",
				  "citizenAdvice": "string"
				}
				"""
				.formatted(safe(request.getComplaintDescription()), safe(request.getWaterSource()), ruleHint);

		String rawJson = callAi(systemPrompt, userPrompt);

		try {
			String cleaned = stripJsonFences(rawJson);
			ComplaintClassificationResult result = objectMapper.readValue(cleaned, ComplaintClassificationResult.class);
			return new AiAssistantResponse(objectMapper.writeValueAsString(result));
		} catch (Exception e) {
			log.warn("Could not parse structured complaint JSON, returning raw model output: {}", e.getMessage());
			return new AiAssistantResponse(rawJson);
		}
	}

	public AiAssistantResponse explainAlert(Long alertId, String role, String language) {

		Alert alert = requireAlert(alertId);

		String systemPrompt = buildSystemPrompt(role, language);

		String userPrompt = """
				Explain this alert in simple language.

				Alert ID: %s
				Village: %s
				Alert Type: %s
				Risk Level: %s
				Status: %s
				Message: %s

				Give answer in this format:
				1. What happened?
				2. Why is it risky?
				3. What citizens should do?
				4. What officers should do?
				""".formatted(alert.getId(), alert.getVillage().getVillageName(), alert.getAlertType(),
				alert.getRiskLevel(), alert.getStatus(), alert.getMessage());

		String answer = callAi(systemPrompt, userPrompt);

		return new AiAssistantResponse(answer);
	}

	/**
	 * Compact, SMS-safe (<=160 char) version of an alert, suitable for bulk SMS
	 * gateways.
	 */
	public AiAssistantResponse smsAlertSummary(Long alertId, String language) {

		Alert alert = requireAlert(alertId);

		String systemPrompt = buildSystemPrompt("CITIZEN", language)
				+ "\nOutput must fit in one SMS message (max 160 characters). No greeting, no sign-off, no extra text.";

		String userPrompt = """
				Create a citizen SMS alert.

				Village: %s
				Alert Type: %s
				Risk Level: %s
				Message: %s

				Rules:
				- Max 160 characters total, including spaces.
				- Plain, urgent, and clear.
				- Include exactly one concrete action step.
				""".formatted(alert.getVillage().getVillageName(), alert.getAlertType(), alert.getRiskLevel(),
				alert.getMessage());

		String answer = callAi(systemPrompt, userPrompt).trim();

		if (answer.length() > 160) {
			answer = answer.substring(0, 157).trim() + "...";
		}

		return new AiAssistantResponse(answer);
	}

	public AiAssistantResponse generateReport(AiAssistantRequest request) {

		requireVillageId(request.getVillageId());

		String systemPrompt = buildSystemPrompt(request.getRole(), request.getLanguage());
		String villageData = buildVillageData(request.getVillageId());

		String reportType = request.getReportType() == null ? "VILLAGE_SUMMARY" : request.getReportType();

		String userPrompt = """
				Generate JalRakshak report.

				Report Type:
				%s

				Give report in this format:
				1. Report Title
				2. Village Overview
				3. Complaint Summary
				4. Health Summary
				5. Water Quality Summary
				6. Alert Summary
				7. Risk Conclusion
				8. Recommended Actions

				Village Data:
				%s
				""".formatted(reportType, villageData);

		String answer = callAi(systemPrompt, userPrompt);

		return new AiAssistantResponse(answer);
	}

	// =========================================================================================
	// NEW FEATURE: multi-turn conversational chat with per-session memory
	// =========================================================================================

	/**
	 * Multi-turn chat: keeps a rolling conversation history per sessionId so
	 * citizens/officers can ask follow-up questions ("what about the handpump near
	 * ward 3?") without repeating context. History is capped at
	 * MAX_HISTORY_MESSAGES to bound token usage/cost.
	 */
	public AiAssistantResponse chat(String sessionId, AiAssistantRequest request) {

		if (sessionId == null || sessionId.isBlank()) {
			throw new IllegalArgumentException("sessionId is required for chat()");
		}

		List<Message> history = conversationStore.computeIfAbsent(sessionId, k -> new ArrayList<>());

		if (history.isEmpty()) {
			history.add(new SystemMessage(buildSystemPrompt(request.getRole(), request.getLanguage())));
		}

		history.add(new UserMessage(safe(request.getQuestion())));
		trimHistory(history);

		String answer;
		try {
			answer = chatClient.prompt(new Prompt(new ArrayList<>(history))).call().content();
		} catch (Exception e) {
			log.error("Multi-turn chat call failed for session {}", sessionId, e);
			answer = "AI service is currently not available. Error: " + e.getMessage();
			// Don't poison history with a failed exchange.
			history.remove(history.size() - 1);
			return new AiAssistantResponse(answer);
		}

		history.add(new AssistantMessage(answer));
		trimHistory(history);

		return new AiAssistantResponse(answer);
	}

	public void clearConversation(String sessionId) {
		conversationStore.remove(sessionId);
	}

	private void trimHistory(List<Message> history) {
		// Always keep index 0 (the system message); drop the oldest user/assistant
		// turns first.
		while (history.size() > MAX_HISTORY_MESSAGES) {
			history.remove(1);
		}
	}

	// =========================================================================================
	// NEW FEATURE: multi-village comparison (useful for HEALTH_OFFICER / ADMIN
	// dashboards)
	// =========================================================================================

	public AiAssistantResponse compareVillages(List<Long> villageIds, String role, String language) {

		if (villageIds == null || villageIds.isEmpty()) {
			throw new IllegalArgumentException("At least one villageId is required for comparison");
		}

		String systemPrompt = buildSystemPrompt(role, language);

		StringBuilder combined = new StringBuilder();
		for (Long id : villageIds) {
			combined.append("--- Village ID ").append(id).append(" ---\n");
			combined.append(buildVillageData(id)).append("\n\n");
		}

		String userPrompt = """
				Compare the following villages and rank them by overall risk (highest risk first).

				Give answer in this format:
				1. Risk Ranking (Village Name - Priority)
				2. Common Issues Across Villages
				3. Village Needing Most Urgent Attention
				4. Comparative Recommendation

				Use only given data. Do not invent fake data.

				Villages Data:
				%s
				""".formatted(combined);

		String answer = callAi(systemPrompt, userPrompt);

		return new AiAssistantResponse(answer);
	}

	// =========================================================================================
	// Shared helpers
	// =========================================================================================

	private String buildVillageData(Long villageId) {

		Village village = villageService.getVillageById(villageId);

		List<Complaint> complaints = complaintRepository.findByVillage_Id(villageId);
		List<HealthCaseReport> healthCases = healthCaseRepository.findByVillage_Id(villageId);
		List<WaterQualityReport> waterReports = waterQualityRepository.findByVillage_Id(villageId);
		List<Alert> alerts = alertRepository.findByVillage_Id(villageId);

		StringBuilder data = new StringBuilder();

		data.append("Village Name: ").append(village.getVillageName()).append("\n");
		data.append("District: ").append(village.getDistrict()).append("\n");
		data.append("State: ").append(village.getState()).append("\n\n");

		data.append("Complaints:\n");

		if (complaints.isEmpty()) {
			data.append("- No complaints found\n");
		} else {
			for (Complaint c : complaints) {
				data.append("- ID: ").append(c.getId()).append(", Type: ").append(c.getComplaintType())
						.append(", Status: ").append(c.getStatus()).append(", Ward: ").append(c.getWardNumber())
						.append(", Water Source: ").append(c.getWaterSource()).append(", Description: ")
						.append(c.getDescription()).append("\n");
			}
		}

		data.append("\nHealth Cases:\n");

		if (healthCases.isEmpty()) {
			data.append("- No health cases found\n");
		} else {
			for (HealthCaseReport h : healthCases) {
				data.append("- ID: ").append(h.getId()).append(", Symptom: ").append(h.getSymptomType())
						.append(", Age Group: ").append(h.getAgeGroup()).append(", Case Count: ")
						.append(h.getCaseCount()).append(", Ward: ").append(h.getWardNumber()).append(", Description: ")
						.append(h.getDescription()).append("\n");
			}
		}

		data.append("\nWater Quality Reports:\n");

		if (waterReports.isEmpty()) {
			data.append("- No water reports found\n");
		} else {
			for (WaterQualityReport w : waterReports) {
				data.append("- ID: ").append(w.getId()).append(", Source: ").append(w.getWaterSourceType())
						.append(", pH: ").append(w.getPhLevel()).append(", Turbidity: ").append(w.getTurbidity())
						.append(", Chlorine: ").append(w.getChlorineLevel()).append(", Color Issue: ")
						.append(w.getColorIssue()).append(", Smell Issue: ").append(w.getSmellIssue())
						.append(", Risk: ").append(w.getRiskLevel()).append(", Remarks: ").append(w.getRemarks())
						.append("\n");
			}
		}

		data.append("\nAlerts:\n");

		if (alerts.isEmpty()) {
			data.append("- No alerts found\n");
		} else {
			for (Alert a : alerts) {
				data.append("- ID: ").append(a.getId()).append(", Type: ").append(a.getAlertType()).append(", Risk: ")
						.append(a.getRiskLevel()).append(", Status: ").append(a.getStatus()).append(", Message: ")
						.append(a.getMessage()).append("\n");
			}
		}

		return data.toString();
	}

	private String buildSystemPrompt(String role, String language) {

		return """
				You are JalRakshak AI Assistant.

				JalRakshak is a rural water quality and health alert system.

				User Role:
				%s

				Language Instruction:
				%s

				Rules:
				- Give answer according to user role.
				- Use simple words.
				- Do not create fake data.
				- Use only provided village data when data is given.
				- For health issues, give safe general advice only.
				- Do not prescribe medicine dosage.
				- For serious symptoms, advise contacting ASHA worker, doctor, or nearest health center.
				- Keep answer practical and action-based.

				Role Behavior:
				CITIZEN: Explain in very simple language and tell what to do immediately.
				ASHA_WORKER: Focus on symptoms, case tracking, household visit, and health reporting.
				PANCHAYAT_OFFICER: Focus on water source cleaning, repair, testing, and public announcement.
				HEALTH_OFFICER: Focus on outbreak control, medical team, case monitoring, and escalation.
				ADMIN: Give complete summary, priority, and management action.
				""".formatted(role == null ? "CITIZEN" : role, getLanguageInstruction(language));
	}

	private String getLanguageInstruction(String language) {

		if (language == null) {
			return "Answer in English.";
		}

		String lang = language.trim().toUpperCase();

		return switch (lang) {
		case "HINDI" -> "Answer in Hindi using Devanagari script.";
		case "HINGLISH" -> "Answer in Hinglish using simple Hindi-English mixed language.";
		case "MARATHI" -> "Answer in Marathi using Devanagari script.";
		case "GUJARATI" -> "Answer in Gujarati using Gujarati script.";
		case "BENGALI" -> "Answer in Bengali using Bengali script.";
		case "TAMIL" -> "Answer in Tamil using Tamil script.";
		case "TELUGU" -> "Answer in Telugu using Telugu script.";
		case "KANNADA" -> "Answer in Kannada using Kannada script.";
		case "PUNJABI" -> "Answer in Punjabi using Gurmukhi script.";
		case "ODIA" -> "Answer in Odia using Odia script.";
		default -> "Answer in English.";
		};
	}

	/**
	 * Fast, deterministic keyword-based urgency pre-check. Used to sanity-check /
	 * accompany the AI's own classification in classifyComplaintStructured() so a
	 * single bad model response can't silently downgrade a genuinely urgent
	 * complaint (e.g. mentions of children, blood, or no water supply).
	 */
	private String ruleBasedUrgencyHint(String description) {

		String text = description == null ? "" : description.toLowerCase();

		if (text.contains("child") || text.contains("infant") || text.contains("diarrhea") || text.contains("vomit")
				|| text.contains("blood")) {
			return "HIGH";
		}

		if (text.contains("no water") || text.contains("pipeline burst") || text.contains("dry for")) {
			return "HIGH";
		}

		if (text.contains("smell") || text.contains("color") || text.contains("dirty")) {
			return "MEDIUM";
		}

		return "LOW";
	}

	private String stripJsonFences(String raw) {
		return raw.trim().replaceAll("^```json", "").replaceAll("^```", "").replaceAll("```$", "").trim();
	}

	private Alert requireAlert(Long alertId) {
		if (alertId == null) {
			throw new IllegalArgumentException("alertId is required");
		}
		return alertRepository.findById(alertId)
				.orElseThrow(() -> new RuntimeException("Alert not found with id: " + alertId));
	}

	private void requireVillageId(Long villageId) {
		if (villageId == null) {
			throw new IllegalArgumentException("villageId is required");
		}
	}

	/**
	 * Calls the model with basic retry + exponential backoff so transient
	 * network/API errors don't immediately surface as a failure to the user.
	 */
	private String callAi(String systemPrompt, String userPrompt) {

		Exception lastError = null;

		for (int attempt = 0; attempt <= MAX_RETRIES; attempt++) {
			try {
				return chatClient.prompt().system(systemPrompt).user(userPrompt).call().content();
			} catch (Exception e) {
				lastError = e;
				log.warn("AI call failed (attempt {}/{}): {}", attempt + 1, MAX_RETRIES + 1, e.getMessage());

				if (attempt < MAX_RETRIES) {
					try {
						Thread.sleep(300L * (attempt + 1));
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
						break;
					}
				}
			}
		}

		log.error("AI service unavailable after {} attempts", MAX_RETRIES + 1, lastError);
		return "AI service is currently not available. Error: "
				+ (lastError == null ? "unknown error" : lastError.getMessage());
	}

	private String safe(String value) {
		return value == null || value.isBlank() ? "Not provided" : value;
	}

	/**
	 * Simple TTL cache entry for village risk summaries.
	 */
	private record CacheEntry(String value, long timestamp) {
		boolean isExpired() {
			return System.currentTimeMillis() - timestamp > RISK_CACHE_TTL_MS;
		}
	}
}
