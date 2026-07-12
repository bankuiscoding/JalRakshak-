package com.jalrakshak.jalrakshak.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jalrakshak.jalrakshak.dto.AlertResponse;
import com.jalrakshak.jalrakshak.dto.AssignAlertRequest;
import com.jalrakshak.jalrakshak.dto.UpdateAlertStatusRequest;
import com.jalrakshak.jalrakshak.model.AlertStatus;
import com.jalrakshak.jalrakshak.model.AlertType;
import com.jalrakshak.jalrakshak.model.RiskLevel;
import com.jalrakshak.jalrakshak.service.AlertService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin("*")
public class AlertController {

	private final AlertService alertService;

	public AlertController(AlertService alertService) {
		this.alertService = alertService;
	}

	@PostMapping("/generate/village/{villageId}")
	public AlertResponse generateAlertForVillage(@PathVariable Long villageId) {
		return alertService.generateAlertForVillage(villageId);
	}

	@GetMapping
	public List<AlertResponse> getAllAlerts() {
		return alertService.getAllAlerts();
	}

	@GetMapping("/{id}")
	public AlertResponse getAlertById(@PathVariable Long id) {
		return alertService.getAlertById(id);
	}

	@GetMapping("/village/{villageId}")
	public List<AlertResponse> getAlertsByVillage(@PathVariable Long villageId) {
		return alertService.getAlertsByVillage(villageId);
	}

	@GetMapping("/status/{status}")
	public List<AlertResponse> getAlertsByStatus(@PathVariable AlertStatus status) {
		return alertService.getAlertsByStatus(status);
	}

	@GetMapping("/risk/{riskLevel}")
	public List<AlertResponse> getAlertsByRiskLevel(@PathVariable RiskLevel riskLevel) {
		return alertService.getAlertsByRiskLevel(riskLevel);
	}

	@GetMapping("/type/{alertType}")
	public List<AlertResponse> getAlertsByAlertType(@PathVariable AlertType alertType) {
		return alertService.getAlertsByAlertType(alertType);
	}

	@PutMapping("/{id}/status")
	public AlertResponse updateAlertStatus(@PathVariable Long id,
			@Valid @RequestBody UpdateAlertStatusRequest request) {
		return alertService.updateAlertStatus(id, request.getStatus());
	}

	@PutMapping("/{id}/assign")
	public AlertResponse assignAlert(@PathVariable Long id, @Valid @RequestBody AssignAlertRequest request) {
		return alertService.assignAlert(id, request.getAssignedToUserId());
	}
}