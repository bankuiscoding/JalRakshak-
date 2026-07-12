package com.jalrakshak.jalrakshak.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jalrakshak.jalrakshak.dto.HealthCaseRequest;
import com.jalrakshak.jalrakshak.dto.HealthCaseResponse;
import com.jalrakshak.jalrakshak.model.SymptomType;
import com.jalrakshak.jalrakshak.service.HealthCaseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/health-cases")
@CrossOrigin("*")
public class HealthCaseController {

	private final HealthCaseService healthCaseService;

	public HealthCaseController(HealthCaseService healthCaseService) {
		this.healthCaseService = healthCaseService;
	}

	@PostMapping
	public HealthCaseResponse createHealthCase(@Valid @RequestBody HealthCaseRequest request) {
		return healthCaseService.createHealthCase(request);
	}

	@GetMapping
	public List<HealthCaseResponse> getAllHealthCases() {
		return healthCaseService.getAllHealthCases();
	}

	@GetMapping("/{id}")
	public HealthCaseResponse getHealthCaseById(@PathVariable Long id) {
		return healthCaseService.getHealthCaseById(id);
	}

	@GetMapping("/village/{villageId}")
	public List<HealthCaseResponse> getHealthCasesByVillage(@PathVariable Long villageId) {
		return healthCaseService.getHealthCasesByVillage(villageId);
	}

	@GetMapping("/user/{userId}")
	public List<HealthCaseResponse> getHealthCasesByUser(@PathVariable Long userId) {
		return healthCaseService.getHealthCasesByUser(userId);
	}

	@GetMapping("/symptom/{symptomType}")
	public List<HealthCaseResponse> getHealthCasesBySymptom(@PathVariable SymptomType symptomType) {
		return healthCaseService.getHealthCasesBySymptom(symptomType);
	}
}