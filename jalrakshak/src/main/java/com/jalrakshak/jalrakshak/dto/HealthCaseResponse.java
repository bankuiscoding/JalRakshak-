package com.jalrakshak.jalrakshak.dto;

import java.time.LocalDateTime;

import com.jalrakshak.jalrakshak.model.AgeGroup;
import com.jalrakshak.jalrakshak.model.SymptomType;

public class HealthCaseResponse {

	private Long id;
	private SymptomType symptomType;
	private AgeGroup ageGroup;
	private Integer caseCount;
	private String wardNumber;
	private String description;
	private Long villageId;
	private String villageName;
	private Long reportedByUserId;
	private String reportedByName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public HealthCaseResponse() {

	}

	public HealthCaseResponse(Long id, SymptomType symptomType, AgeGroup ageGroup, Integer caseCount, String wardNumber,
			String description, Long villageId, String villageName, Long reportedByUserId, String reportedByName,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.symptomType = symptomType;
		this.ageGroup = ageGroup;
		this.caseCount = caseCount;
		this.wardNumber = wardNumber;
		this.description = description;
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

	public void setId(Long id) {
		this.id = id;
	}

	public SymptomType getSymptomType() {
		return symptomType;
	}

	public void setSymptomType(SymptomType symptomType) {
		this.symptomType = symptomType;
	}

	public AgeGroup getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(AgeGroup ageGroup) {
		this.ageGroup = ageGroup;
	}

	public Integer getCaseCount() {
		return caseCount;
	}

	public void setCaseCount(Integer caseCount) {
		this.caseCount = caseCount;
	}

	public String getWardNumber() {
		return wardNumber;
	}

	public void setWardNumber(String wardNumber) {
		this.wardNumber = wardNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getVillageId() {
		return villageId;
	}

	public void setVillageId(Long villageId) {
		this.villageId = villageId;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public Long getReportedByUserId() {
		return reportedByUserId;
	}

	public void setReportedByUserId(Long reportedByUserId) {
		this.reportedByUserId = reportedByUserId;
	}

	public String getReportedByName() {
		return reportedByName;
	}

	public void setReportedByName(String reportedByName) {
		this.reportedByName = reportedByName;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}
