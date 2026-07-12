package com.jalrakshak.jalrakshak.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jalrakshak.jalrakshak.dto.AdminDashboardResponse;
import com.jalrakshak.jalrakshak.dto.VillageDashboardResponse;
import com.jalrakshak.jalrakshak.model.Alert;
import com.jalrakshak.jalrakshak.model.AlertStatus;
import com.jalrakshak.jalrakshak.model.Complaint;
import com.jalrakshak.jalrakshak.model.ComplaintStatus;
import com.jalrakshak.jalrakshak.model.HealthCaseReport;
import com.jalrakshak.jalrakshak.model.RiskLevel;
import com.jalrakshak.jalrakshak.model.Village;
import com.jalrakshak.jalrakshak.model.WaterQualityReport;
import com.jalrakshak.jalrakshak.repository.AlertRepository;
import com.jalrakshak.jalrakshak.repository.ComplaintRepository;
import com.jalrakshak.jalrakshak.repository.HealthCaseRepository;
import com.jalrakshak.jalrakshak.repository.UserRepository;
import com.jalrakshak.jalrakshak.repository.VillageRepository;
import com.jalrakshak.jalrakshak.repository.WaterQualityRepository;

@Service
public class DashboardService {

	private final VillageRepository villageRepository;
	private final UserRepository userRepository;
	private final ComplaintRepository complaintRepository;
	private final HealthCaseRepository healthCaseRepository;
	private final WaterQualityRepository waterQualityRepository;
	private final AlertRepository alertRepository;
	private final VillageService villageService;

	public DashboardService(VillageRepository villageRepository, UserRepository userRepository,
			ComplaintRepository complaintRepository, HealthCaseRepository healthCaseRepository,
			WaterQualityRepository waterQualityRepository, AlertRepository alertRepository,
			VillageService villageService) {
		this.villageRepository = villageRepository;
		this.userRepository = userRepository;
		this.complaintRepository = complaintRepository;
		this.healthCaseRepository = healthCaseRepository;
		this.waterQualityRepository = waterQualityRepository;
		this.alertRepository = alertRepository;
		this.villageService = villageService;
	}

	public AdminDashboardResponse getAdminDashboard() {

		long totalVillages = villageRepository.count();
		long totalUsers = userRepository.count();

		long totalComplaints = complaintRepository.count();
		long pendingComplaints = complaintRepository.findByStatus(ComplaintStatus.PENDING).size();
		long inProgressComplaints = complaintRepository.findByStatus(ComplaintStatus.IN_PROGRESS).size();
		long resolvedComplaints = complaintRepository.findByStatus(ComplaintStatus.RESOLVED).size();

		List<HealthCaseReport> healthReports = healthCaseRepository.findAll();

		long totalHealthReports = healthReports.size();

		long totalHealthCaseCount = healthReports.stream()
				.mapToInt(report -> report.getCaseCount() != null ? report.getCaseCount() : 0).sum();

		long totalWaterReports = waterQualityRepository.count();
		long safeWaterReports = waterQualityRepository.findByRiskLevel(RiskLevel.SAFE).size();
		long warningWaterReports = waterQualityRepository.findByRiskLevel(RiskLevel.WARNING).size();
		long dangerousWaterReports = waterQualityRepository.findByRiskLevel(RiskLevel.DANGEROUS).size();

		long totalAlerts = alertRepository.count();
		long openAlerts = alertRepository.findByStatus(AlertStatus.OPEN).size();
		long assignedAlerts = alertRepository.findByStatus(AlertStatus.ASSIGNED).size();
		long resolvedAlerts = alertRepository.findByStatus(AlertStatus.RESOLVED).size();
		long dangerousAlerts = alertRepository.findByRiskLevel(RiskLevel.DANGEROUS).size();

		return new AdminDashboardResponse(totalVillages, totalUsers, totalComplaints, pendingComplaints,
				inProgressComplaints, resolvedComplaints, totalHealthReports, totalHealthCaseCount, totalWaterReports,
				safeWaterReports, warningWaterReports, dangerousWaterReports, totalAlerts, openAlerts, assignedAlerts,
				resolvedAlerts, dangerousAlerts);
	}

	public VillageDashboardResponse getVillageDashboard(Long villageId) {

		Village village = villageService.getVillageById(villageId);

		List<Complaint> complaints = complaintRepository.findByVillage_Id(villageId);
		List<HealthCaseReport> healthReports = healthCaseRepository.findByVillage_Id(villageId);
		List<WaterQualityReport> waterReports = waterQualityRepository.findByVillage_Id(villageId);
		List<Alert> alerts = alertRepository.findByVillage_Id(villageId);

		long totalComplaints = complaints.size();

		long pendingComplaints = complaints.stream().filter(c -> c.getStatus() == ComplaintStatus.PENDING).count();

		long resolvedComplaints = complaints.stream().filter(c -> c.getStatus() == ComplaintStatus.RESOLVED).count();

		long totalHealthReports = healthReports.size();

		long totalHealthCaseCount = healthReports.stream()
				.mapToInt(report -> report.getCaseCount() != null ? report.getCaseCount() : 0).sum();

		long totalWaterReports = waterReports.size();

		long safeWaterReports = waterReports.stream().filter(w -> w.getRiskLevel() == RiskLevel.SAFE).count();

		long warningWaterReports = waterReports.stream().filter(w -> w.getRiskLevel() == RiskLevel.WARNING).count();

		long dangerousWaterReports = waterReports.stream().filter(w -> w.getRiskLevel() == RiskLevel.DANGEROUS).count();

		long totalAlerts = alerts.size();

		long openAlerts = alerts.stream().filter(a -> a.getStatus() == AlertStatus.OPEN).count();

		long dangerousAlerts = alerts.stream().filter(a -> a.getRiskLevel() == RiskLevel.DANGEROUS).count();

		return new VillageDashboardResponse(village.getId(), village.getVillageName(), totalComplaints,
				pendingComplaints, resolvedComplaints, totalHealthReports, totalHealthCaseCount, totalWaterReports,
				safeWaterReports, warningWaterReports, dangerousWaterReports, totalAlerts, openAlerts, dangerousAlerts);
	}
}