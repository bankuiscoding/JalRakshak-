package com.jalrakshak.jalrakshak.dto;

import com.jalrakshak.jalrakshak.model.ComplaintStatus;

import jakarta.validation.constraints.NotNull;

public class UpdateComplaintStatusRequest {

	@NotNull
	private ComplaintStatus status;

	public UpdateComplaintStatusRequest() {
	}

	public ComplaintStatus getStatus() {
		return status;
	}

	public void setStatus(ComplaintStatus status) {
		this.status = status;
	}
}