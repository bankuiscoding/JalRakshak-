package com.jalrakshak.jalrakshak.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private final JavaMailSender javaMailSender;

	@Value("${MAIL_FROM:${spring.mail.username}}")
	private String fromEmail;

	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void sendSimpleEmail(String to, String subject, String body) {
		if (to == null || to.isBlank()) {
			return;
		}

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(fromEmail);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(body);

			javaMailSender.send(message);
			System.out.println("Email sent successfully to: " + to);

		} catch (Exception e) {
			System.out.println("Email sending failed: " + e.getMessage());
		}
	}
}