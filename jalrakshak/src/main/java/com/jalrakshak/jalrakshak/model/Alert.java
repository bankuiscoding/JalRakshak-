package com.jalrakshak.jalrakshak.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "alerts")
public class Alert {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private AlertType alertType;

	@Enumerated(EnumType.STRING)
	private RiskLevel riskLevel;

	@Column(length = 1500)
	private String message;

	@Enumerated(EnumType.STRING)
	private AlertStatus status;

	@ManyToOne
	@JoinColumn(name = "village_id")
	private Village village;

	@ManyToOne
	@JoinColumn(name = "assigned_to")
	private User assignedTo;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public Alert() {
	}

	@PrePersist
	public void beforeSave() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();

		if (this.status == null) {
			this.status = AlertStatus.OPEN;
		}
	}

	@PreUpdate
	public void beforeUpdate() {
		this.updatedAt = LocalDateTime.now();
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

	public Village getVillage() {
		return village;
	}

	public User getAssignedTo() {
		return assignedTo;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setAlertType(AlertType alertType) {
		this.alertType = alertType;
	}

	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatus(AlertStatus status) {
		this.status = status;
	}

	public void setVillage(Village village) {
		this.village = village;
	}

	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}