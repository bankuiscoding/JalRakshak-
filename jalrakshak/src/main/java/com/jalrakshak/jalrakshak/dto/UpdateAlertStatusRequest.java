package com.jalrakshak.jalrakshak.dto;

import com.jalrakshak.jalrakshak.model.AlertStatus;

import jakarta.validation.constraints.NotNull;

public class UpdateAlertStatusRequest {

	@NotNull
	private AlertStatus status;

	public UpdateAlertStatusRequest() {
	}

	public AlertStatus getStatus() {
		return status;
	}

	public void setStatus(AlertStatus status) {
		this.status = status;
	}
}