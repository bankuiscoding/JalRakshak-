package com.jalrakshak.jalrakshak.dto;

import java.time.LocalDateTime;

import com.jalrakshak.jalrakshak.model.AlertStatus;
import com.jalrakshak.jalrakshak.model.AlertType;
import com.jalrakshak.jalrakshak.model.RiskLevel;

public class AlertResponse {

	private Long id;
	private AlertType alertType;
	private RiskLevel riskLevel;
	private String message;
	private AlertStatus status;

	private Long villageId;
	private String villageName;

	private Long assignedToUserId;
	private String assignedToName;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public AlertResponse() {
	}

	public AlertResponse(Long id, AlertType alertType, RiskLevel riskLevel, String message, AlertStatus status,
			Long villageId, String villageName, Long assignedToUserId, String assignedToName, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		this.id = id;
		this.alertType = alertType;
		this.riskLevel = riskLevel;
		this.message = message;
		this.status = status;
		this.villageId = villageId;
		this.villageName = villageName;
		this.assignedToUserId = assignedToUserId;
		this.assignedToName = assignedToName;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public AlertType getAlertType() {
		return alertType;
	}

	public RiskLevel getRiskLevel() {
		return riskLevel;
	}

	public String getMessage() {
		return message;
	}

	public AlertStatus getStatus() {
		return status;
	}

	public Long getVillageId() {
		return villageId;
	}

	public String getVillageName() {
		return villageName;
	}

	public Long getAssignedToUserId() {
		return assignedToUserId;
	}

	public String getAssignedToName() {
		return assignedToName;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}