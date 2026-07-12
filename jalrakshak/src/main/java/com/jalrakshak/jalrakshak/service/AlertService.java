package com.jalrakshak.jalrakshak.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jalrakshak.jalrakshak.dto.AlertResponse;
import com.jalrakshak.jalrakshak.model.Alert;
import com.jalrakshak.jalrakshak.model.AlertStatus;
import com.jalrakshak.jalrakshak.model.AlertType;
import com.jalrakshak.jalrakshak.model.Complaint;
import com.jalrakshak.jalrakshak.model.ComplaintType;
import com.jalrakshak.jalrakshak.model.HealthCaseReport;
import com.jalrakshak.jalrakshak.model.RiskLevel;
import com.jalrakshak.jalrakshak.model.SymptomType;
import com.jalrakshak.jalrakshak.model.User;
import com.jalrakshak.jalrakshak.model.Village;
import com.jalrakshak.jalrakshak.model.WaterQualityReport;
import com.jalrakshak.jalrakshak.repository.AlertRepository;
import com.jalrakshak.jalrakshak.repository.ComplaintRepository;
import com.jalrakshak.jalrakshak.repository.HealthCaseRepository;
import com.jalrakshak.jalrakshak.repository.WaterQualityRepository;

@Service
public class AlertService {

	private final AlertRepository alertRepository;
	private final VillageService villageService;
	private final UserService userService;
	private final ComplaintRepository complaintRepository;
	private final HealthCaseRepository healthCaseRepository;
	private final WaterQualityRepository waterQualityRepository;
	private final NotificationService notificationService;

	public AlertService(AlertRepository alertRepository, VillageService villageService, UserService userService,
			ComplaintRepository complaintRepository, HealthCaseRepository healthCaseRepository,
			WaterQualityRepository waterQualityRepository, NotificationService notificationService) {

		this.alertRepository = alertRepository;
		this.villageService = villageService;
		this.userService = userService;
		this.complaintRepository = complaintRepository;
		this.healthCaseRepository = healthCaseRepository;
		this.waterQualityRepository = waterQualityRepository;
		this.notificationService = notificationService;
	}

	public AlertResponse generateAlertForVillage(Long villageId) {

		Village village = villageService.getVillageById(villageId);

		List<Complaint> complaints = complaintRepository.findByVillage_Id(villageId);
		List<HealthCaseReport> healthCases = healthCaseRepository.findByVillage_Id(villageId);
		List<WaterQualityReport> waterReports = waterQualityRepository.findByVillage_Id(villageId);

		long dirtyWaterComplaints = complaints.stream()
				.filter(c -> c.getComplaintType() == ComplaintType.DIRTY_WATER
						|| c.getComplaintType() == ComplaintType.BAD_SMELL
						|| c.getComplaintType() == ComplaintType.TANK_CLEANING_ISSUE)
				.count();

		int waterRelatedHealthCases = healthCases.stream()
				.filter(h -> h.getSymptomType() == SymptomType.DIARRHEA || h.getSymptomType() == SymptomType.VOMITING
						|| h.getSymptomType() == SymptomType.STOMACH_PAIN)
				.mapToInt(h -> h.getCaseCount() != null ? h.getCaseCount() : 0).sum();

		boolean hasDangerousWaterReport = waterReports.stream().anyMatch(w -> w.getRiskLevel() == RiskLevel.DANGEROUS);

		boolean hasWarningWaterReport = waterReports.stream().anyMatch(w -> w.getRiskLevel() == RiskLevel.WARNING);

		Alert alert = new Alert();
		alert.setVillage(village);
		alert.setStatus(AlertStatus.OPEN);

		if (hasDangerousWaterReport || (dirtyWaterComplaints >= 3 && waterRelatedHealthCases >= 5)) {

			alert.setRiskLevel(RiskLevel.DANGEROUS);

			if (dirtyWaterComplaints >= 3 && waterRelatedHealthCases >= 5) {
				alert.setAlertType(AlertType.HEALTH_OUTBREAK);
				alert.setMessage("High risk alert for " + village.getVillageName()
						+ ". Multiple dirty water complaints and water-related health cases reported. "
						+ "Possible water contamination or disease outbreak.");
			} else {
				alert.setAlertType(AlertType.WATER_QUALITY);
				alert.setMessage("Dangerous water quality report found for " + village.getVillageName()
						+ ". Immediate water testing and action required.");
			}

		} else if (hasWarningWaterReport || dirtyWaterComplaints >= 2 || waterRelatedHealthCases >= 3) {

			alert.setRiskLevel(RiskLevel.WARNING);

			if (hasWarningWaterReport) {
				alert.setAlertType(AlertType.WATER_QUALITY);
				alert.setMessage("Warning alert for " + village.getVillageName()
						+ ". Water quality report indicates risk. Monitoring required.");
			} else if (dirtyWaterComplaints >= 2) {
				alert.setAlertType(AlertType.COMPLAINT_CLUSTER);
				alert.setMessage("Warning alert for " + village.getVillageName()
						+ ". Multiple water complaints reported from this village.");
			} else {
				alert.setAlertType(AlertType.HEALTH_OUTBREAK);
				alert.setMessage("Warning alert for " + village.getVillageName()
						+ ". Water-related symptoms reported. Health officer should review.");
			}

		} else {

			alert.setRiskLevel(RiskLevel.SAFE);
			alert.setAlertType(AlertType.GENERAL);
			alert.setStatus(AlertStatus.RESOLVED);
			alert.setMessage("No major risk found for " + village.getVillageName()
					+ ". Current complaints, health cases, and water reports are under control.");
		}

		Alert savedAlert = alertRepository.save(alert);

		notificationService.sendAlertToOfficials(savedAlert);
		notificationService.sendAlertToVillageUsers(savedAlert);

		return mapToResponse(savedAlert);
	}

	public List<AlertResponse> getAllAlerts() {
		return alertRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	public AlertResponse getAlertById(Long id) {
		Alert alert = alertRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Alert not found with id: " + id));

		return mapToResponse(alert);
	}

	public List<AlertResponse> getAlertsByVillage(Long villageId) {
		return alertRepository.findByVillage_Id(villageId).stream().map(this::mapToResponse).toList();
	}

	public List<AlertResponse> getAlertsByStatus(AlertStatus status) {
		return alertRepository.findByStatus(status).stream().map(this::mapToResponse).toList();
	}

	public List<AlertResponse> getAlertsByRiskLevel(RiskLevel riskLevel) {
		return alertRepository.findByRiskLevel(riskLevel).stream().map(this::mapToResponse).toList();
	}

	public List<AlertResponse> getAlertsByAlertType(AlertType alertType) {
		return alertRepository.findByAlertType(alertType).stream().map(this::mapToResponse).toList();
	}

	public AlertResponse updateAlertStatus(Long alertId, AlertStatus status) {

		Alert alert = alertRepository.findById(alertId)
				.orElseThrow(() -> new RuntimeException("Alert not found with id: " + alertId));

		alert.setStatus(status);

		Alert updatedAlert = alertRepository.save(alert);

		return mapToResponse(updatedAlert);
	}

	public AlertResponse assignAlert(Long alertId, Long assignedToUserId) {

		Alert alert = alertRepository.findById(alertId)
				.orElseThrow(() -> new RuntimeException("Alert not found with id: " + alertId));

		User assignedUser = userService.getUserById(assignedToUserId);

		alert.setAssignedTo(assignedUser);
		alert.setStatus(AlertStatus.ASSIGNED);

		Alert updatedAlert = alertRepository.save(alert);

		notificationService.sendAlertAssignedNotification(updatedAlert, assignedUser);

		return mapToResponse(updatedAlert);
	}

	private AlertResponse mapToResponse(Alert alert) {

		Long assignedUserId = null;
		String assignedUserName = null;

		if (alert.getAssignedTo() != null) {
			assignedUserId = alert.getAssignedTo().getId();
			assignedUserName = alert.getAssignedTo().getName();
		}

		return new AlertResponse(alert.getId(), alert.getAlertType(), alert.getRiskLevel(), alert.getMessage(),
				alert.getStatus(), alert.getVillage().getId(), alert.getVillage().getVillageName(), assignedUserId,
				assignedUserName, alert.getCreatedAt(), alert.getUpdatedAt());
	}
}