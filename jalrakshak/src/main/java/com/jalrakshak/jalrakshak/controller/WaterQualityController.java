package com.jalrakshak.jalrakshak.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jalrakshak.jalrakshak.dto.WaterQualityRequest;
import com.jalrakshak.jalrakshak.dto.WaterQualityResponse;
import com.jalrakshak.jalrakshak.model.RiskLevel;
import com.jalrakshak.jalrakshak.model.WaterSourceType;
import com.jalrakshak.jalrakshak.service.WaterQualityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/water-quality")
@CrossOrigin("*")
public class WaterQualityController {

	private final WaterQualityService waterQualityService;

	public WaterQualityController(WaterQualityService waterQualityService) {
		this.waterQualityService = waterQualityService;
	}

	@PostMapping
	public WaterQualityResponse createWaterQualityReport(@Valid @RequestBody WaterQualityRequest request) {
		return waterQualityService.createWaterQualityReport(request);
	}

	@GetMapping
	public List<WaterQualityResponse> getAllWaterQualityReports() {
		return waterQualityService.getAllWaterQualityReports();
	}

	@GetMapping("/{id}")
	public WaterQualityResponse getWaterQualityReportById(@PathVariable Long id) {
		return waterQualityService.getWaterQualityReportById(id);
	}

	@GetMapping("/village/{villageId}")
	public List<WaterQualityResponse> getReportsByVillage(@PathVariable Long villageId) {
		return waterQualityService.getReportsByVillage(villageId);
	}

	@GetMapping("/user/{userId}")
	public List<WaterQualityResponse> getReportsByUser(@PathVariable Long userId) {
		return waterQualityService.getReportsByUser(userId);
	}

	@GetMapping("/risk/{riskLevel}")
	public List<WaterQualityResponse> getReportsByRiskLevel(@PathVariable RiskLevel riskLevel) {
		return waterQualityService.getReportsByRiskLevel(riskLevel);
	}

	@GetMapping("/source/{waterSourceType}")
	public List<WaterQualityResponse> getReportsByWaterSourceType(@PathVariable WaterSourceType waterSourceType) {
		return waterQualityService.getReportsByWaterSourceType(waterSourceType);
	}
}