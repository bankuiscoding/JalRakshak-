package com.jalrakshak.jalrakshak.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jalrakshak.jalrakshak.dto.NotificationResponse;
import com.jalrakshak.jalrakshak.model.Alert;
import com.jalrakshak.jalrakshak.model.Complaint;
import com.jalrakshak.jalrakshak.model.Notification;
import com.jalrakshak.jalrakshak.model.RiskLevel;
import com.jalrakshak.jalrakshak.model.Role;
import com.jalrakshak.jalrakshak.model.User;
import com.jalrakshak.jalrakshak.repository.NotificationRepository;

@Service
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final UserService userService;
	private final EmailService emailService;

	public NotificationService(NotificationRepository notificationRepository, UserService userService,
			EmailService emailService) {
		this.notificationRepository = notificationRepository;
		this.userService = userService;
		this.emailService = emailService;
	}

	public void sendComplaintRegisteredNotification(Complaint complaint) {

		String title = "Complaint Registered";
		String message = "Your complaint has been registered successfully. Complaint ID: " + complaint.getId()
				+ ". Current status: " + complaint.getStatus();

		saveNotification(complaint.getReportedBy(), null, title, message);

		emailService.sendSimpleEmail(complaint.getReportedBy().getEmail(), "JalRakshak - Complaint Registered",
				"Hello " + complaint.getReportedBy().getName() + ",\n\n" + message + "\n\n" + "Village: "
						+ complaint.getVillage().getVillageName() + "\n" + "Complaint Type: "
						+ complaint.getComplaintType() + "\n\n" + "Thank you,\nJalRakshak Team");
	}

	public void sendComplaintStatusChangedNotification(Complaint complaint) {

		String title = "Complaint Status Updated";
		String message = "Your complaint ID " + complaint.getId() + " status has been changed to "
				+ complaint.getStatus();

		saveNotification(complaint.getReportedBy(), null, title, message);

		emailService.sendSimpleEmail(complaint.getReportedBy().getEmail(), "JalRakshak - Complaint Status Updated",
				"Hello " + complaint.getReportedBy().getName() + ",\n\n" + message + "\n\n"
						+ "Thank you,\nJalRakshak Team");
	}

	public void sendAlertToOfficials(Alert alert) {

		if (alert.getRiskLevel() == RiskLevel.SAFE) {
			return;
		}

		List<User> usersToNotify = new ArrayList<>();

		usersToNotify.addAll(userService.getUsersByRole(Role.HEALTH_OFFICER));
		usersToNotify.addAll(userService.getUsersByRole(Role.PANCHAYAT_OFFICER));
		usersToNotify.addAll(userService.getUsersByRole(Role.ADMIN));

		for (User user : usersToNotify) {

			String title = "New " + alert.getRiskLevel() + " Alert";
			String message = alert.getMessage();

			saveNotification(user, alert, title, message);

			emailService.sendSimpleEmail(user.getEmail(), "JalRakshak - " + title,
					"Hello " + user.getName() + ",\n\n" + message + "\n\n" + "Village: "
							+ alert.getVillage().getVillageName() + "\n" + "Risk Level: " + alert.getRiskLevel() + "\n"
							+ "Alert Type: " + alert.getAlertType() + "\n\n" + "Please check JalRakshak dashboard.\n\n"
							+ "Thank you,\nJalRakshak Team");
		}
	}

	public void sendAlertToVillageUsers(Alert alert) {

		if (alert.getRiskLevel() == RiskLevel.SAFE) {
			return;
		}

		Long villageId = alert.getVillage().getId();

		List<User> villageUsers = userService.getUsersByVillage(villageId);

		for (User user : villageUsers) {

			String title = "Village Alert: " + alert.getRiskLevel();
			String message = alert.getMessage();

			saveNotification(user, alert, title, message);

			emailService.sendSimpleEmail(user.getEmail(), "JalRakshak - Village Alert",
					"Hello " + user.getName() + ",\n\n" + message + "\n\n" + "Village: "
							+ alert.getVillage().getVillageName() + "\n" + "Risk Level: " + alert.getRiskLevel()
							+ "\n\n" + "Please avoid unsafe water and follow official instructions.\n\n"
							+ "Thank you,\nJalRakshak Team");
		}
	}

	public void sendAlertAssignedNotification(Alert alert, User assignedUser) {

		String title = "Alert Assigned";
		String message = "An alert has been assigned to you: " + alert.getMessage();

		saveNotification(assignedUser, alert, title, message);

		emailService.sendSimpleEmail(assignedUser.getEmail(), "JalRakshak - Alert Assigned",
				"Hello " + assignedUser.getName() + ",\n\n" + message + "\n\n" + "Village: "
						+ alert.getVillage().getVillageName() + "\n" + "Risk Level: " + alert.getRiskLevel() + "\n\n"
						+ "Please check JalRakshak dashboard.\n\n" + "Thank you,\nJalRakshak Team");
	}

	public List<NotificationResponse> getNotificationsByUser(Long userId) {
		return notificationRepository.findByUser_IdOrderByCreatedAtDesc(userId).stream().map(this::mapToResponse)
				.toList();
	}

	public List<NotificationResponse> getUnreadNotificationsByUser(Long userId) {
		return notificationRepository.findByUser_IdAndReadStatusFalseOrderByCreatedAtDesc(userId).stream()
				.map(this::mapToResponse).toList();
	}

	public NotificationResponse markAsRead(Long notificationId) {

		Notification notification = notificationRepository.findById(notificationId)
				.orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

		notification.setReadStatus(true);

		Notification savedNotification = notificationRepository.save(notification);

		return mapToResponse(savedNotification);
	}

	private void saveNotification(User user, Alert alert, String title, String message) {

		Notification notification = new Notification();
		notification.setTitle(title);
		notification.setMessage(message);
		notification.setUser(user);
		notification.setAlert(alert);
		notification.setReadStatus(false);

		notificationRepository.save(notification);
	}

	private NotificationResponse mapToResponse(Notification notification) {

		Long alertId = null;

		if (notification.getAlert() != null) {
			alertId = notification.getAlert().getId();
		}

		return new NotificationResponse(notification.getId(), notification.getTitle(), notification.getMessage(),
				notification.getReadStatus(), notification.getUser().getId(), notification.getUser().getName(), alertId,
				notification.getCreatedAt());
	}
}