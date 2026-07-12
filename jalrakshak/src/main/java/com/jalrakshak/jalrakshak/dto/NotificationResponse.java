package com.jalrakshak.jalrakshak.dto;

import java.time.LocalDateTime;

public class NotificationResponse {

	private Long id;
	private String title;
	private String message;
	private Boolean readStatus;

	private Long userId;
	private String userName;

	private Long alertId;

	private LocalDateTime createdAt;

	public NotificationResponse() {
	}

	public NotificationResponse(Long id, String title, String message, Boolean readStatus, Long userId, String userName,
			Long alertId, LocalDateTime createdAt) {
		this.id = id;
		this.title = title;
		this.message = message;
		this.readStatus = readStatus;
		this.userId = userId;
		this.userName = userName;
		this.alertId = alertId;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public Boolean getReadStatus() {
		return readStatus;
	}

	public Long getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public Long getAlertId() {
		return alertId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}