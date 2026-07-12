package com.jalrakshak.jalrakshak.dto;

import java.time.LocalDateTime;

import com.jalrakshak.jalrakshak.model.ComplaintStatus;
import com.jalrakshak.jalrakshak.model.ComplaintType;

public class ComplaintResponse {

	private Long id;
	private ComplaintType complaintType;
	private String description;
	private String wardNumber;
	private String waterSource;
	private ComplaintStatus status;

	private Long villageId;
	private String villageName;

	private Long reportedByUserId;
	private String reportedByName;
	private String photoUrl;
	private String photoPublicId;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;


	public ComplaintResponse() {
	}

	public ComplaintResponse(Long id, ComplaintType complaintType, String description, String wardNumber,
			String waterSource, ComplaintStatus status, Long villageId, String villageName, Long reportedByUserId,
			String photoUrl, String photoPublicId,
			String reportedByName, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.complaintType = complaintType;
		this.description = description;
		this.wardNumber = wardNumber;
		this.waterSource = waterSource;
		this.status = status;
		this.villageId = villageId;
		this.villageName = villageName;
		this.reportedByUserId = reportedByUserId;
		this.reportedByName = reportedByName;
		this.photoUrl = photoUrl;
		this.photoPublicId = photoPublicId;

		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public ComplaintType getComplaintType() {
		return complaintType;
	}

	public String getDescription() {
		return description;
	}

	public String getWardNumber() {
		return wardNumber;
	}

	public String getWaterSource() {
		return waterSource;
	}

	public ComplaintStatus getStatus() {
		return status;
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

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getPhotoPublicId() {
		return photoPublicId;
	}

	public void setPhotoPublicId(String photoPublicId) {
		this.photoPublicId = photoPublicId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}