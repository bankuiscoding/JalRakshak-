package com.jalrakshak.jalrakshak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jalrakshak.jalrakshak.model.RiskLevel;
import com.jalrakshak.jalrakshak.model.WaterQualityReport;
import com.jalrakshak.jalrakshak.model.WaterSourceType;

public interface WaterQualityRepository extends JpaRepository<WaterQualityReport, Long> {

	List<WaterQualityReport> findByVillage_Id(Long villageId);

	List<WaterQualityReport> findByReportedBy_Id(Long userId);

	List<WaterQualityReport> findByRiskLevel(RiskLevel riskLevel);

	List<WaterQualityReport> findByWaterSourceType(WaterSourceType waterSourceType);
}