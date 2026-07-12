package com.jalrakshak.jalrakshak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jalrakshak.jalrakshak.model.Alert;
import com.jalrakshak.jalrakshak.model.AlertStatus;
import com.jalrakshak.jalrakshak.model.AlertType;
import com.jalrakshak.jalrakshak.model.RiskLevel;

public interface AlertRepository extends JpaRepository<Alert, Long> {

	List<Alert> findByVillage_Id(Long villageId);

	List<Alert> findByStatus(AlertStatus status);

	List<Alert> findByRiskLevel(RiskLevel riskLevel);

	List<Alert> findByAlertType(AlertType alertType);
}