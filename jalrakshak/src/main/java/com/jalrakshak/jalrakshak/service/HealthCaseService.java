package com.jalrakshak.jalrakshak.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jalrakshak.jalrakshak.dto.HealthCaseRequest;
import com.jalrakshak.jalrakshak.dto.HealthCaseResponse;
import com.jalrakshak.jalrakshak.model.HealthCaseReport;
import com.jalrakshak.jalrakshak.model.SymptomType;
import com.jalrakshak.jalrakshak.model.User;
import com.jalrakshak.jalrakshak.model.Village;
import com.jalrakshak.jalrakshak.repository.HealthCaseRepository;

@Service
public class HealthCaseService {

	private final HealthCaseRepository healthCaseRepository;
	private final VillageService villageService;
	private final UserService userService;

	public HealthCaseService(HealthCaseRepository healthCaseRepository, VillageService villageService,
			UserService userService) {
		this.healthCaseRepository = healthCaseRepository;
		this.villageService = villageService;
		this.userService = userService;
	}

	public HealthCaseResponse createHealthCase(HealthCaseRequest request) {

		Village village = villageService.getVillageById(request.getVillageId());
		User user = userService.getUserById(request.getReportedByUserId());

		HealthCaseReport report = new HealthCaseReport();
		report.setSymptomType(request.getSymptomType());
		report.setAgeGroup(request.getAgeGroup());
		report.setCaseCount(request.getCaseCount());
		report.setWardNumber(request.getWardNumber());
		report.setDescription(request.getDescription());
		report.setVillage(village);
		report.setReportedBy(user);

		HealthCaseReport savedReport = healthCaseRepository.save(report);

		return mapToResponse(savedReport);
	}

	public List<HealthCaseResponse> getAllHealthCases() {
		return healthCaseRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	public HealthCaseResponse getHealthCaseById(Long id) {
		HealthCaseReport report = healthCaseRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Health case report not found with id: " + id));

		return mapToResponse(report);
	}

	public List<HealthCaseResponse> getHealthCasesByVillage(Long villageId) {
		return healthCaseRepository.findByVillage_Id(villageId).stream().map(this::mapToResponse).toList();
	}

	public List<HealthCaseResponse> getHealthCasesByUser(Long userId) {
		return healthCaseRepository.findByReportedBy_Id(userId).stream().map(this::mapToResponse).toList();

	}

	public List<HealthCaseResponse> getHealthCasesBySymptom(SymptomType symptomType) {
		return healthCaseRepository.findBySymptomType(symptomType).stream().map(this::mapToResponse).toList();
	}

	private HealthCaseResponse mapToResponse(HealthCaseReport report) {

		return new HealthCaseResponse(report.getId(), report.getSymptomType(), report.getAgeGroup(),
				report.getCaseCount(), report.getWardNumber(), report.getDescription(), report.getVillage().getId(),
				report.getVillage().getVillageName(), report.getReportedBy().getId(), report.getReportedBy().getName(),
				report.getCreatedAt(), report.getUpdatedAt());
	}
}