package com.jalrakshak.jalrakshak.dto;

import com.jalrakshak.jalrakshak.model.Role;

public class AuthResponse {

	private Long userId;
	private String name;
	private String email;
	private String mobileNumber;
	private Role role;
	private String message;

	public AuthResponse() {
	}

	public AuthResponse(Long userId, String name, String email, String mobileNumber, Role role, String message) {
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.role = role;
		this.message = message;
	}

	public Long getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public Role getRole() {
		return role;
	}

	public String getMessage() {
		return message;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}