package com.jalrakshak.jalrakshak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jalrakshak.jalrakshak.model.HealthCaseReport;
import com.jalrakshak.jalrakshak.model.SymptomType;

public interface HealthCaseRepository extends JpaRepository<HealthCaseReport, Long> {

	List<HealthCaseReport> findByVillage_Id(Long villageId);

	List<HealthCaseReport> findByReportedBy_Id(Long userId);

	List<HealthCaseReport> findBySymptomType(SymptomType symptomType);
}