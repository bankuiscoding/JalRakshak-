package com.jalrakshak.jalrakshak.dto;

import com.jalrakshak.jalrakshak.model.ComplaintType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ComplaintRequest {

	@NotNull
	private ComplaintType complaintType;

	@NotBlank
	private String description;

	private String wardNumber;

	private String waterSource;

	@NotNull

	private Long villageId;

	@NotNull
	private Long reportedByUserId;

	public ComplaintRequest() {

	}

	public ComplaintType getComplaintType() {
		return complaintType;
	}

	public void setComplaintType(ComplaintType complaintType) {
		this.complaintType = complaintType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWardNumber() {
		return wardNumber;
	}

	public void setWardNumber(String wardNumber) {
		this.wardNumber = wardNumber;
	}

	public String getWaterSource() {
		return waterSource;
	}

	public void setWaterSource(String waterSource) {
		this.waterSource = waterSource;
	}

	public Long getVillageId() {
		return villageId;
	}

	public void setVillageId(Long villageId) {
		this.villageId = villageId;
	}

	public Long getReportedByUserId() {
		return reportedByUserId;
	}

	public void setReportedByUserId(Long reportedByUserId) {
		this.reportedByUserId = reportedByUserId;
	}

}
