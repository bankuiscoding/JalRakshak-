package com.jalrakshak.jalrakshak.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {

	@Value("${BREVO_API_KEY}")
	private String brevoApiKey;

	@Value("${MAIL_FROM}")
	private String fromEmail;

	private final RestTemplate restTemplate = new RestTemplate();

	public void sendSimpleEmail(String to, String subject, String body) {
		if (to == null || to.isBlank()) {
			return;
		}

		try {
			String url = "https://api.brevo.com/v3/smtp/email";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("api-key", brevoApiKey);

			Map<String, Object> requestBody = Map.of("sender", Map.of("name", "JalRakshak", "email", fromEmail), "to",
					List.of(Map.of("email", to)), "subject", subject, "textContent", body);

			HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

			restTemplate.postForEntity(url, request, String.class);

			System.out.println("Email sent successfully to: " + to);

		} catch (Exception e) {
			System.out.println("Email sending failed: " + e.getMessage());
		}
	}
}