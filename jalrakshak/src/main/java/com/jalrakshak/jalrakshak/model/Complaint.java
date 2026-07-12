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
@Table(name = "complaints")
public class Complaint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private ComplaintType complaintType;

	@Column(length = 1000)
	private String description;

	private String wardNumber;

	private String waterSource;

	@Enumerated(EnumType.STRING)
	private ComplaintStatus status;

	@ManyToOne
	@JoinColumn(name = "village_id")
	private Village village;

	@ManyToOne
	@JoinColumn(name = "reported_by")
	private User reportedBy;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String photoUrl;
	private String photoPublicId;

	public Complaint() {
	}

	@PrePersist
	public void beforeSave() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();

		if (this.status == null) {
			this.status = ComplaintStatus.PENDING;
		}
	}

	@PreUpdate
	public void beforeUpdate() {
		this.updatedAt = LocalDateTime.now();
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

	public void setComplaintType(ComplaintType complaintType) {
		this.complaintType = complaintType;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setWardNumber(String wardNumber) {
		this.wardNumber = wardNumber;
	}

	public void setWaterSource(String waterSource) {
		this.waterSource = waterSource;
	}

	public void setStatus(ComplaintStatus status) {
		this.status = status;
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
}