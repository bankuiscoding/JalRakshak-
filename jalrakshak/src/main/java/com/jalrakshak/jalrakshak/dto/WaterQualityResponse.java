package com.jalrakshak.jalrakshak.dto;

import java.time.LocalDateTime;

import com.jalrakshak.jalrakshak.model.RiskLevel;
import com.jalrakshak.jalrakshak.model.WaterSourceType;

public class WaterQualityResponse {

	private Long id;
	private WaterSourceType waterSourceType;
	private Double phLevel;
	private Double turbidity;
	private Double chlorineLevel;
	private Boolean colorIssue;
	private Boolean smellIssue;
	private String remarks;
	private RiskLevel riskLevel;

	private Long villageId;
	private String villageName;

	private Long reportedByUserId;
	private String reportedByName;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public WaterQualityResponse() {
	}

	public WaterQualityResponse(Long id, WaterSourceType waterSourceType, Double phLevel, Double turbidity,
			Double chlorineLevel, Boolean colorIssue, Boolean smellIssue, String remarks, RiskLevel riskLevel,
			Long villageId, String villageName, Long reportedByUserId, String reportedByName, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		this.id = id;
		this.waterSourceType = waterSourceType;
		this.phLevel = phLevel;
		this.turbidity = turbidity;
		this.chlorineLevel = chlorineLevel;
		this.colorIssue = colorIssue;
		this.smellIssue = smellIssue;
		this.remarks = remarks;
		this.riskLevel = riskLevel;
		this.villageId = villageId;
		this.villageName = villageName;
		this.reportedByUserId = reportedByUserId;
		this.reportedByName = reportedByName;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
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

	public Long getVillageId() {
		return villageId;
	}

	public String getVillageName() {
		return villageName;
	}

	public Long getReportedByUserId() {
		return reportedByUserId;
	}

	public String getReportedByName() {
		return reportedByName;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}