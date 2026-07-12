package com.jalrakshak.jalrakshak.dto;

public class AiAssistantRequest {

	private String question;
	private String role;
	private String language;

	private Long villageId;
	private Long alertId;

	private String symptoms;
	private String ageGroup;
	private Integer caseCount;

	private String complaintDescription;
	private String waterSource;

	private String reportType;

	public AiAssistantRequest() {
	}

	public String getQuestion() {
		return question;
	}

	public String getRole() {
		return role;
	}

	public String getLanguage() {
		return language;
	}

	public Long getVillageId() {
		return villageId;
	}

	public Long getAlertId() {
		return alertId;
	}

	public String getSymptoms() {
		return symptoms;
	}

	public String getAgeGroup() {
		return ageGroup;
	}

	public Integer getCaseCount() {
		return caseCount;
	}

	public String getComplaintDescription() {
		return complaintDescription;
	}

	public String getWaterSource() {
		return waterSource;
	}

	public String getReportType() {
		return reportType;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setVillageId(Long villageId) {
		this.villageId = villageId;
	}

	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}

	public void setCaseCount(Integer caseCount) {
		this.caseCount = caseCount;
	}

	public void setComplaintDescription(String complaintDescription) {
		this.complaintDescription = complaintDescription;
	}

	public void setWaterSource(String waterSource) {
		this.waterSource = waterSource;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
}