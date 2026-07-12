package com.jalrakshak.jalrakshak.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jalrakshak.jalrakshak.dto.CloudinaryUploadResponse;
import com.jalrakshak.jalrakshak.dto.ComplaintRequest;
import com.jalrakshak.jalrakshak.dto.ComplaintResponse;
import com.jalrakshak.jalrakshak.model.Complaint;
import com.jalrakshak.jalrakshak.model.ComplaintStatus;
import com.jalrakshak.jalrakshak.model.User;
import com.jalrakshak.jalrakshak.model.Village;
import com.jalrakshak.jalrakshak.repository.ComplaintRepository;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final VillageService villageService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
	private final NotificationService notificationService;

    public ComplaintService(ComplaintRepository complaintRepository,
			VillageService villageService, UserService userService, CloudinaryService cloudinaryService,
			NotificationService notificationService) {
		this.complaintRepository = complaintRepository;
		this.villageService = villageService;
		this.userService = userService;
		this.cloudinaryService = cloudinaryService;
		this.notificationService = notificationService;
	}

    public ComplaintResponse createComplaint(ComplaintRequest request) {

        Village village = villageService.getVillageById(request.getVillageId());
        User user = userService.getUserById(request.getReportedByUserId());

        Complaint complaint = new Complaint();
        complaint.setComplaintType(request.getComplaintType());
        complaint.setDescription(request.getDescription());
        complaint.setWardNumber(request.getWardNumber());
        complaint.setWaterSource(request.getWaterSource());
        complaint.setVillage(village);
        complaint.setReportedBy(user);
        complaint.setStatus(ComplaintStatus.PENDING);

        Complaint savedComplaint = complaintRepository.save(complaint);

		notificationService.sendComplaintRegisteredNotification(savedComplaint);

        return mapToResponse(savedComplaint);
    }

    public ComplaintResponse createComplaintWithPhoto(ComplaintRequest request, MultipartFile photo) {

        Village village = villageService.getVillageById(request.getVillageId());
        User user = userService.getUserById(request.getReportedByUserId());

        CloudinaryUploadResponse uploadedPhoto = cloudinaryService.uploadComplaintPhoto(photo);

        Complaint complaint = new Complaint();
        complaint.setComplaintType(request.getComplaintType());
        complaint.setDescription(request.getDescription());
        complaint.setWardNumber(request.getWardNumber());
        complaint.setWaterSource(request.getWaterSource());
        complaint.setVillage(village);
        complaint.setReportedBy(user);
        complaint.setStatus(ComplaintStatus.PENDING);

        if (uploadedPhoto != null) {
            complaint.setPhotoUrl(uploadedPhoto.getPhotoUrl());
            complaint.setPhotoPublicId(uploadedPhoto.getPublicId());
        }

        Complaint savedComplaint = complaintRepository.save(complaint);

		notificationService.sendComplaintRegisteredNotification(savedComplaint);

        return mapToResponse(savedComplaint);
    }

    public List<ComplaintResponse> getAllComplaints() {
        return complaintRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ComplaintResponse getComplaintById(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found with id: " + id));

        return mapToResponse(complaint);
    }

    public List<ComplaintResponse> getComplaintsByVillage(Long villageId) {
        return complaintRepository.findByVillage_Id(villageId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ComplaintResponse> getComplaintsByUser(Long userId) {
        return complaintRepository.findByReportedBy_Id(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ComplaintResponse> getComplaintsByStatus(ComplaintStatus status) {
        return complaintRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ComplaintResponse updateComplaintStatus(Long complaintId, ComplaintStatus status) {

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found with id: " + complaintId));

        complaint.setStatus(status);

        Complaint updatedComplaint = complaintRepository.save(complaint);

		notificationService.sendComplaintStatusChangedNotification(updatedComplaint);

        return mapToResponse(updatedComplaint);
    }

    private ComplaintResponse mapToResponse(Complaint complaint) {

        return new ComplaintResponse(
                complaint.getId(),
                complaint.getComplaintType(),
                complaint.getDescription(),
                complaint.getWardNumber(),
                complaint.getWaterSource(),
                complaint.getStatus(),
                complaint.getVillage().getId(),
                complaint.getVillage().getVillageName(),
                complaint.getReportedBy().getId(),
                complaint.getReportedBy().getName(),
                complaint.getPhotoUrl(),
                complaint.getPhotoPublicId(),
                complaint.getCreatedAt(),
                complaint.getUpdatedAt()
        );
    }
}