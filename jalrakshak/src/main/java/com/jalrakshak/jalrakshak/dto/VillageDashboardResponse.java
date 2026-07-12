package com.jalrakshak.jalrakshak.dto;

public class VillageDashboardResponse {

	private Long villageId;
	private String villageName;

	private long totalComplaints;
	private long pendingComplaints;
	private long resolvedComplaints;

	private long totalHealthReports;
	private long totalHealthCaseCount;

	private long totalWaterReports;
	private long safeWaterReports;
	private long warningWaterReports;
	private long dangerousWaterReports;

	private long totalAlerts;
	private long openAlerts;
	private long dangerousAlerts;

	public VillageDashboardResponse() {
	}

	public VillageDashboardResponse(Long villageId, String villageName, long totalComplaints, long pendingComplaints,
			long resolvedComplaints, long totalHealthReports, long totalHealthCaseCount, long totalWaterReports,
			long safeWaterReports, long warningWaterReports, long dangerousWaterReports, long totalAlerts,
			long openAlerts, long dangerousAlerts) {
		this.villageId = villageId;
		this.villageName = villageName;
		this.totalComplaints = totalComplaints;
		this.pendingComplaints = pendingComplaints;
		this.resolvedComplaints = resolvedComplaints;
		this.totalHealthReports = totalHealthReports;
		this.totalHealthCaseCount = totalHealthCaseCount;
		this.totalWaterReports = totalWaterReports;
		this.safeWaterReports = safeWaterReports;
		this.warningWaterReports = warningWaterReports;
		this.dangerousWaterReports = dangerousWaterReports;
		this.totalAlerts = totalAlerts;
		this.openAlerts = openAlerts;
		this.dangerousAlerts = dangerousAlerts;
	}

	public Long getVillageId() {
		return villageId;
	}

	public String getVillageName() {
		return villageName;
	}

	public long getTotalComplaints() {
		return totalComplaints;
	}

	public long getPendingComplaints() {
		return pendingComplaints;
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

	public long getDangerousAlerts() {
		return dangerousAlerts;
	}
}