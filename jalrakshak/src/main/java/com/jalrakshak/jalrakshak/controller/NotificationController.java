package com.jalrakshak.jalrakshak.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jalrakshak.jalrakshak.dto.NotificationResponse;
import com.jalrakshak.jalrakshak.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin("*")
public class NotificationController {

	private final NotificationService notificationService;

	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@GetMapping("/user/{userId}")
	public List<NotificationResponse> getNotificationsByUser(@PathVariable Long userId) {
		return notificationService.getNotificationsByUser(userId);
	}

	@GetMapping("/user/{userId}/unread")
	public List<NotificationResponse> getUnreadNotificationsByUser(@PathVariable Long userId) {
		return notificationService.getUnreadNotificationsByUser(userId);
	}

	@PutMapping("/{notificationId}/read")
	public NotificationResponse markAsRead(@PathVariable Long notificationId) {
		return notificationService.markAsRead(notificationId);
	}
}