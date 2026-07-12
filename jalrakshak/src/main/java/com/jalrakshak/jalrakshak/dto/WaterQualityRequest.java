package com.jalrakshak.jalrakshak.dto;

import com.jalrakshak.jalrakshak.model.WaterSourceType;

import jakarta.validation.constraints.NotNull;

public class WaterQualityRequest {

	@NotNull
	private WaterSourceType waterSourceType;

	@NotNull
	private Double phLevel;

	@NotNull
	private Double turbidity;

	@NotNull
	private Double chlorineLevel;

	private Boolean colorIssue;

	private Boolean smellIssue;

	private String remarks;

	@NotNull
	private Long villageId;

	@NotNull
	private Long reportedByUserId;

	public WaterQualityRequest() {
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

	public Long getVillageId() {
		return villageId;
	}

	public Long getReportedByUserId() {
		return reportedByUserId;
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

	public void setVillageId(Long villageId) {
		this.villageId = villageId;
	}

	public void setReportedByUserId(Long reportedByUserId) {
		this.reportedByUserId = reportedByUserId;
	}
}