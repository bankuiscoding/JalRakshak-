package com.jalrakshak.jalrakshak.dto;

import jakarta.validation.constraints.NotNull;

public class AssignAlertRequest {

	@NotNull
	private Long assignedToUserId;

	public AssignAlertRequest() {
	}

	public Long getAssignedToUserId() {
		return assignedToUserId;
	}

	public void setAssignedToUserId(Long assignedToUserId) {
		this.assignedToUserId = assignedToUserId;
	}
}