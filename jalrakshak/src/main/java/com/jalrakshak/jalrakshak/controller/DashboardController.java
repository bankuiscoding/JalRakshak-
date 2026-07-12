package com.jalrakshak.jalrakshak.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jalrakshak.jalrakshak.dto.AdminDashboardResponse;
import com.jalrakshak.jalrakshak.dto.VillageDashboardResponse;
import com.jalrakshak.jalrakshak.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin("*")
public class DashboardController {

	private final DashboardService dashboardService;

	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	@GetMapping("/admin")
	public AdminDashboardResponse getAdminDashboard() {
		return dashboardService.getAdminDashboard();
	}

	@GetMapping("/village/{villageId}")
	public VillageDashboardResponse getVillageDashboard(@PathVariable Long villageId) {
		return dashboardService.getVillageDashboard(villageId);
	}
}