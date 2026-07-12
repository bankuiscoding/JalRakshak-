package com.jalrakshak.jalrakshak.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class VerifyEmailRequest {

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String otp;

	public VerifyEmailRequest() {
	}

	public String getEmail() {
		return email;
	}

	public String getOtp() {
		return otp;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}
}