package com.jalrakshak.jalrakshak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jalrakshak.jalrakshak.model.Complaint;
import com.jalrakshak.jalrakshak.model.ComplaintStatus;
import com.jalrakshak.jalrakshak.model.ComplaintType;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

	List<Complaint> findByVillage_Id(Long villageId);

	List<Complaint> findByReportedBy_Id(Long userId);

	List<Complaint> findByStatus(ComplaintStatus status);

	List<Complaint> findByComplaintType(ComplaintType complaintType);
}