package com.jalrakshak.jalrakshak.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jalrakshak.jalrakshak.dto.WaterQualityRequest;
import com.jalrakshak.jalrakshak.dto.WaterQualityResponse;
import com.jalrakshak.jalrakshak.model.RiskLevel;
import com.jalrakshak.jalrakshak.model.User;
import com.jalrakshak.jalrakshak.model.Village;
import com.jalrakshak.jalrakshak.model.WaterQualityReport;
import com.jalrakshak.jalrakshak.model.WaterSourceType;
import com.jalrakshak.jalrakshak.repository.WaterQualityRepository;

@Service
public class WaterQualityService {

	private final WaterQualityRepository waterQualityRepository;
	private final VillageService villageService;
	private final UserService userService;

	public WaterQualityService(WaterQualityRepository waterQualityRepository, VillageService villageService,
			UserService userService) {
		this.waterQualityRepository = waterQualityRepository;
		this.villageService = villageService;
		this.userService = userService;
	}

	public WaterQualityResponse createWaterQualityReport(WaterQualityRequest request) {

		Village village = villageService.getVillageById(request.getVillageId());
		User user = userService.getUserById(request.getReportedByUserId());

		RiskLevel riskLevel = calculateRisk(request.getPhLevel(), request.getTurbidity(), request.getChlorineLevel(),
				request.getColorIssue(), request.getSmellIssue());

		WaterQualityReport report = new WaterQualityReport();
		report.setWaterSourceType(request.getWaterSourceType());
		report.setPhLevel(request.getPhLevel());
		report.setTurbidity(request.getTurbidity());
		report.setChlorineLevel(request.getChlorineLevel());
		report.setColorIssue(request.getColorIssue() != null ? request.getColorIssue() : false);
		report.setSmellIssue(request.getSmellIssue() != null ? request.getSmellIssue() : false);
		report.setRemarks(request.getRemarks());
		report.setRiskLevel(riskLevel);
		report.setVillage(village);
		report.setReportedBy(user);

		WaterQualityReport savedReport = waterQualityRepository.save(report);

		return mapToResponse(savedReport);
	}

	public List<WaterQualityResponse> getAllWaterQualityReports() {
		return waterQualityRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	public WaterQualityResponse getWaterQualityReportById(Long id) {
		WaterQualityReport report = waterQualityRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Water quality report not found with id: " + id));

		return mapToResponse(report);
	}

	public List<WaterQualityResponse> getReportsByVillage(Long villageId) {
		return waterQualityRepository.findByVillage_Id(villageId).stream().map(this::mapToResponse).toList();
	}

	public List<WaterQualityResponse> getReportsByUser(Long userId) {
		return waterQualityRepository.findByReportedBy_Id(userId).stream().map(this::mapToResponse).toList();
	}

	public List<WaterQualityResponse> getReportsByRiskLevel(RiskLevel riskLevel) {
		return waterQualityRepository.findByRiskLevel(riskLevel).stream().map(this::mapToResponse).toList();
	}

	public List<WaterQualityResponse> getReportsByWaterSourceType(WaterSourceType waterSourceType) {
		return waterQualityRepository.findByWaterSourceType(waterSourceType).stream().map(this::mapToResponse).toList();
	}

	private RiskLevel calculateRisk(Double phLevel, Double turbidity, Double chlorineLevel, Boolean colorIssue,
			Boolean smellIssue) {

		boolean hasColorIssue = colorIssue != null && colorIssue;
		boolean hasSmellIssue = smellIssue != null && smellIssue;


		if (phLevel < 5.5 || phLevel > 9.5 || turbidity > 10 || chlorineLevel <= 0 || hasColorIssue || hasSmellIssue) {
			return RiskLevel.DANGEROUS;
		}


		if (phLevel < 6.5 || phLevel > 8.5 || turbidity > 5 || chlorineLevel < 0.2) {
			return RiskLevel.WARNING;
		}

		return RiskLevel.SAFE;
	}

	private WaterQualityResponse mapToResponse(WaterQualityReport report) {

		return new WaterQualityResponse(report.getId(), report.getWaterSourceType(), report.getPhLevel(),
				report.getTurbidity(), report.getChlorineLevel(), report.getColorIssue(), report.getSmellIssue(),
				report.getRemarks(), report.getRiskLevel(), report.getVillage().getId(),
				report.getVillage().getVillageName(), report.getReportedBy().getId(), report.getReportedBy().getName(),
				report.getCreatedAt(), report.getUpdatedAt());
	}
}