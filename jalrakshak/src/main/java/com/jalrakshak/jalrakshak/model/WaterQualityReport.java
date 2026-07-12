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
@Table(name = "water_quality_reports")
public class WaterQualityReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private WaterSourceType waterSourceType;

	private Double phLevel;

	private Double turbidity;

	private Double chlorineLevel;

	private Boolean colorIssue;

	private Boolean smellIssue;

	@Column(length = 1000)
	private String remarks;

	@Enumerated(EnumType.STRING)
	private RiskLevel riskLevel;

	@ManyToOne
	@JoinColumn(name = "village_id")
	private Village village;

	@ManyToOne
	@JoinColumn(name = "reported_by")
	private User reportedBy;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public WaterQualityReport() {
	}

	@PrePersist
	public void beforeSave() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	public void beforeUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public WaterSourceType getWaterSourceType() {
		return waterSourceType;
	}

	public Double getPhLevel() {
		return phLevel;
	}

	public Double getTurbidity() {
		return turbidity;
	}

	public Double getChlorineLevel() {
		return chlorineLevel;
	}

	public Boolean getColorIssue() {
		return colorIssue;
	}

	public Boolean getSmellIssue() {
		return smellIssue;
	}

	public String getRemarks() {
		return remarks;
	}

	public RiskLevel getRiskLevel() {
		return riskLevel;
	}

	public Village getVillage() {
		return village;
	}

	public User getReportedBy() {
		return reportedBy;
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

	public void setWaterSourceType(WaterSourceType waterSourceType) {
		this.waterSourceType = waterSourceType;
	}

	public void setPhLevel(Double phLevel) {
		this.phLevel = phLevel;
	}

	public void setTurbidity(Double turbidity) {
		this.turbidity = turbidity;
	}

	public void setChlorineLevel(Double chlorineLevel) {
		this.chlorineLevel = chlorineLevel;
	}

	public void setColorIssue(Boolean colorIssue) {
		this.colorIssue = colorIssue;
	}

	public void setSmellIssue(Boolean smellIssue) {
		this.smellIssue = smellIssue;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}

	public void setVillage(Village village) {
		this.village = village;
	}

	public void setReportedBy(User reportedBy) {
		this.reportedBy = reportedBy;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}