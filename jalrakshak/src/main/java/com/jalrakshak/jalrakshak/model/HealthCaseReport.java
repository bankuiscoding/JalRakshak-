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
@Table(name = "health_case_reports")
public class HealthCaseReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private SymptomType symptomType;

	@Enumerated(EnumType.STRING)
	private AgeGroup ageGroup;

	private Integer caseCount;

	private String wardNumber;

	@Column(length = 1000)
	private String description;

	@ManyToOne
	@JoinColumn(name = "village_id")
	private Village village;

	@ManyToOne
	@JoinColumn(name = "reported_by")
	private User reportedBy;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public HealthCaseReport() {
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

	public SymptomType getSymptomType() {
		return symptomType;
	}

	public AgeGroup getAgeGroup() {
		return ageGroup;
	}

	public Integer getCaseCount() {
		return caseCount;
	}

	public String getWardNumber() {
		return wardNumber;
	}

	public String getDescription() {
		return description;
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

	public void setSymptomType(SymptomType symptomType) {
		this.symptomType = symptomType;
	}

	public void setAgeGroup(AgeGroup ageGroup) {
		this.ageGroup = ageGroup;
	}

	public void setCaseCount(Integer caseCount) {
		this.caseCount = caseCount;
	}

	public void setWardNumber(String wardNumber) {
		this.wardNumber = wardNumber;
	}

	public void setDescription(String description) {
		this.description = description;
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