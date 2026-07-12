package com.jalrakshak.jalrakshak.dto;

import com.jalrakshak.jalrakshak.model.AgeGroup;
import com.jalrakshak.jalrakshak.model.SymptomType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class HealthCaseRequest {

	@NotNull
	private SymptomType symptomType;
	@NotNull
	private AgeGroup ageGroup;

	@NotNull
	@Min(1)
	private Integer caseCount;
	private String wardNumber;
	private String description;
	@NotNull
	private Long villageId;

	@NotNull
	private Long reportedByUserId;

	public HealthCaseRequest() {

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

	public Long getReportedByUserId() {
		return reportedByUserId;
	}

	public void setReportedByUserId(Long reportedByUserId) {
		this.reportedByUserId = reportedByUserId;
	}

}
