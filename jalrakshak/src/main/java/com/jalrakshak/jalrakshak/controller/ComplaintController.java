package com.jalrakshak.jalrakshak.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jalrakshak.jalrakshak.dto.ComplaintRequest;
import com.jalrakshak.jalrakshak.dto.ComplaintResponse;
import com.jalrakshak.jalrakshak.model.ComplaintStatus;
import com.jalrakshak.jalrakshak.model.ComplaintType;
import com.jalrakshak.jalrakshak.service.ComplaintService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin("*")
public class ComplaintController {

	private final ComplaintService complaintService;

	public ComplaintController(ComplaintService complaintService) {
		this.complaintService = complaintService;
	}

	@PostMapping
	public ComplaintResponse createComplaint(@Valid @RequestBody ComplaintRequest request) {
		return complaintService.createComplaint(request);
	}

	@GetMapping
	public List<ComplaintResponse> getAllComplaints() {
		return complaintService.getAllComplaints();
	}

	@GetMapping("/{id}")
	public ComplaintResponse getComplaintById(@PathVariable Long id) {
		return complaintService.getComplaintById(id);
	}

	@GetMapping("/village/{villageId}")
	public List<ComplaintResponse> getComplaintsByVillage(@PathVariable Long villageId) {
		return complaintService.getComplaintsByVillage(villageId);
	}

	@GetMapping("/user/{userId}")
	public List<ComplaintResponse> getComplaintsByUser(@PathVariable Long userId) {
		return complaintService.getComplaintsByUser(userId);
	}

	@GetMapping("/status/{status}")
	public List<ComplaintResponse> getComplaintsByStatus(@PathVariable ComplaintStatus status) {
		return complaintService.getComplaintsByStatus(status);
	}

	@PostMapping(value = "/with-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ComplaintResponse createComplaintWithPhoto(@RequestParam ComplaintType complaintType,
			@RequestParam String description, @RequestParam(required = false) String wardNumber,
			@RequestParam(required = false) String waterSource, @RequestParam Long villageId,
			@RequestParam Long reportedByUserId, @RequestParam(required = false) MultipartFile photo) {
		ComplaintRequest request = new ComplaintRequest();
		request.setComplaintType(complaintType);
		request.setDescription(description);
		request.setWardNumber(wardNumber);
		request.setWaterSource(waterSource);
		request.setVillageId(villageId);
		request.setReportedByUserId(reportedByUserId);

		return complaintService.createComplaintWithPhoto(request, photo);
	}

	@GetMapping("/visible-to-user/{userId}")
	public List<ComplaintResponse> getComplaintsVisibleToUser(@PathVariable Long userId) {
		return complaintService.getComplaintsVisibleToUser(userId);
	}

	@PutMapping("/{complaintId}/status/user/{userId}")
	public ComplaintResponse updateComplaintStatusByUser(@PathVariable Long complaintId, @PathVariable Long userId,
			@RequestParam ComplaintStatus status) {

		return complaintService.updateComplaintStatusByUser(complaintId, status, userId);
	}
}