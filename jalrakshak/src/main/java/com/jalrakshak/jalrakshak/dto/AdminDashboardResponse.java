package com.jalrakshak.jalrakshak.dto;

public class AdminDashboardResponse {

	private long totalVillages;
	private long totalUsers;

	private long totalComplaints;
	private long pendingComplaints;
	private long inProgressComplaints;
	private long resolvedComplaints;

	private long totalHealthReports;
	private long totalHealthCaseCount;

	private long totalWaterReports;
	private long safeWaterReports;
	private long warningWaterReports;
	private long dangerousWaterReports;

	private long totalAlerts;
	private long openAlerts;
	private long assignedAlerts;
	private long resolvedAlerts;
	private long dangerousAlerts;

	public AdminDashboardResponse() {
	}

	public AdminDashboardResponse(long totalVillages, long totalUsers, long totalComplaints, long pendingComplaints,
			long inProgressComplaints, long resolvedComplaints, long totalHealthReports, long totalHealthCaseCount,
			long totalWaterReports, long safeWaterReports, long warningWaterReports, long dangerousWaterReports,
			long totalAlerts, long openAlerts, long assignedAlerts, long resolvedAlerts, long dangerousAlerts) {
		this.totalVillages = totalVillages;
		this.totalUsers = totalUsers;
		this.totalComplaints = totalComplaints;
		this.pendingComplaints = pendingComplaints;
		this.inProgressComplaints = inProgressComplaints;
		this.resolvedComplaints = resolvedComplaints;
		this.totalHealthReports = totalHealthReports;
		this.totalHealthCaseCount = totalHealthCaseCount;
		this.totalWaterReports = totalWaterReports;
		this.safeWaterReports = safeWaterReports;
		this.warningWaterReports = warningWaterReports;
		this.dangerousWaterReports = dangerousWaterReports;
		this.totalAlerts = totalAlerts;
		this.openAlerts = openAlerts;
		this.assignedAlerts = assignedAlerts;
		this.resolvedAlerts = resolvedAlerts;
		this.dangerousAlerts = dangerousAlerts;
	}

	public long getTotalVillages() {
		return totalVillages;
	}

	public long getTotalUsers() {
		return totalUsers;
	}

	public long getTotalComplaints() {
		return totalComplaints;
	}

	public long getPendingComplaints() {
		return pendingComplaints;
	}

	public long getInProgressComplaints() {
		return inProgressComplaints;
	}

	public long getResolvedComplaints() {
		return resolvedComplaints;
	}

	public long getTotalHealthReports() {
		return totalHealthReports;
	}

	public long getTotalHealthCaseCount() {
		return totalHealthCaseCount;
	}

	public long getTotalWaterReports() {
		return totalWaterReports;
	}

	public long getSafeWaterReports() {
		return safeWaterReports;
	}

	public long getWarningWaterReports() {
		return warningWaterReports;
	}

	public long getDangerousWaterReports() {
		return dangerousWaterReports;
	}

	public long getTotalAlerts() {
		return totalAlerts;
	}

	public long getOpenAlerts() {
		return openAlerts;
	}

	public long getAssignedAlerts() {
		return assignedAlerts;
	}

	public long getResolvedAlerts() {
		return resolvedAlerts;
	}

	public long getDangerousAlerts() {
		return dangerousAlerts;
	}
}