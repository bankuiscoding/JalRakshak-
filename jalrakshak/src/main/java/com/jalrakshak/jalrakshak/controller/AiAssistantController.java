package com.jalrakshak.jalrakshak.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jalrakshak.jalrakshak.dto.AiAssistantRequest;
import com.jalrakshak.jalrakshak.dto.AiAssistantResponse;
import com.jalrakshak.jalrakshak.service.AiAssistantService;

import reactor.core.publisher.Flux;

@RestController
@CrossOrigin("*")
public class AiAssistantController {

	private final AiAssistantService aiAssistantService;

	public AiAssistantController(AiAssistantService aiAssistantService) {
		this.aiAssistantService = aiAssistantService;
	}

	// =========================================================================================
	// Core Q&A
	// =========================================================================================

	@PostMapping("/api/ai/ask")
	public AiAssistantResponse ask(@RequestBody AiAssistantRequest request) {
		return aiAssistantService.askAssistant(request);
	}

	@PostMapping("/api/ai/ask/async")
	public CompletableFuture<AiAssistantResponse> askAsync(@RequestBody AiAssistantRequest request) {
		return aiAssistantService.askAssistantAsync(request);
	}

	/**
	 * Streams the answer token-by-token as Server-Sent Events. Works with plain
	 * Spring MVC (reactor-core is already on the classpath via spring-ai) — WebFlux
	 * is not required, but adding spring-boot-starter-webflux makes SSE handling
	 * more efficient under load.
	 */
	@PostMapping(value = "/api/ai/ask/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> askStream(@RequestBody AiAssistantRequest request) {
		return aiAssistantService.streamAssistant(request);
	}

	// =========================================================================================
	// Multi-turn chat (per-session memory)
	// =========================================================================================

	@PostMapping("/api/ai/chat/{sessionId}")
	public AiAssistantResponse chat(@PathVariable String sessionId, @RequestBody AiAssistantRequest request) {
		return aiAssistantService.chat(sessionId, request);
	}

	@PostMapping("/api/ai/chat/{sessionId}/clear")
	public void clearChat(@PathVariable String sessionId) {
		aiAssistantService.clearConversation(sessionId);
	}

	// =========================================================================================
	// Village risk summary (cached)
	// =========================================================================================

	@GetMapping("/api/ai/village/{villageId}/risk-summary")
	public AiAssistantResponse villageRiskSummary(@PathVariable Long villageId,
			@RequestParam(required = false) String role, @RequestParam(required = false) String language,
			@RequestParam(defaultValue = "false") boolean forceRefresh) {
		return aiAssistantService.villageRiskSummary(villageId, role, language, forceRefresh);
	}

	@PostMapping("/api/ai/village/{villageId}/risk-summary/invalidate-cache")
	public void invalidateRiskCache(@PathVariable Long villageId) {
		aiAssistantService.invalidateRiskCache(villageId);
	}

	// =========================================================================================
	// Health advice
	// =========================================================================================

	@PostMapping("/api/ai/health-advice")
	public AiAssistantResponse healthAdvice(@RequestBody AiAssistantRequest request) {
		return aiAssistantService.healthAdvice(request);
	}

	// =========================================================================================
	// Officer action plan
	// =========================================================================================

	@GetMapping("/api/ai/village/{villageId}/officer-action-plan")
	public AiAssistantResponse officerActionPlan(@PathVariable Long villageId,
			@RequestParam(required = false) String role, @RequestParam(required = false) String language) {
		return aiAssistantService.officerActionPlan(villageId, role, language);
	}

	// =========================================================================================
	// Complaint classification
	// =========================================================================================

	@PostMapping("/api/ai/complaint/classify")
	public AiAssistantResponse classifyComplaint(@RequestBody AiAssistantRequest request) {
		return aiAssistantService.classifyComplaint(request);
	}

	@PostMapping("/api/ai/complaint/classify/structured")
	public AiAssistantResponse classifyComplaintStructured(@RequestBody AiAssistantRequest request) {
		return aiAssistantService.classifyComplaintStructured(request);
	}

	// =========================================================================================
	// Alerts
	// =========================================================================================

	@GetMapping("/api/ai/alert/{alertId}/explain")
	public AiAssistantResponse explainAlert(@PathVariable Long alertId, @RequestParam(required = false) String role,
			@RequestParam(required = false) String language) {
		return aiAssistantService.explainAlert(alertId, role, language);
	}

	@GetMapping("/api/ai/alert/{alertId}/sms-summary")
	public AiAssistantResponse smsAlertSummary(@PathVariable Long alertId,
			@RequestParam(required = false) String language) {
		return aiAssistantService.smsAlertSummary(alertId, language);
	}

	// =========================================================================================
	// Reports
	// =========================================================================================

	@PostMapping("/api/ai/report/generate")
	public AiAssistantResponse generateReport(@RequestBody AiAssistantRequest request) {
		return aiAssistantService.generateReport(request);
	}

	// =========================================================================================
	// Multi-village comparison
	// =========================================================================================

	@GetMapping("/api/ai/villages/compare")
	public AiAssistantResponse compareVillages(@RequestParam List<Long> villageIds,
			@RequestParam(required = false) String role, @RequestParam(required = false) String language) {
		return aiAssistantService.compareVillages(villageIds, role, language);
	}
}