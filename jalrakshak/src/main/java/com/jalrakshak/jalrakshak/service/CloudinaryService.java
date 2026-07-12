package com.jalrakshak.jalrakshak.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.jalrakshak.jalrakshak.dto.CloudinaryUploadResponse;

@Service
public class CloudinaryService {

	private final Cloudinary cloudinary;

	public CloudinaryService(Cloudinary cloudinary) {
		this.cloudinary = cloudinary;
	}

	public CloudinaryUploadResponse uploadComplaintPhoto(MultipartFile file) {

		if (file == null || file.isEmpty()) {
			return null;
		}

		String contentType = file.getContentType();

		if (contentType == null || !contentType.startsWith("image/")) {
			throw new RuntimeException("Only image files are allowed");
		}

		try {
			Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
					ObjectUtils.asMap("folder", "jalrakshak/complaints", "resource_type", "image"));

			String secureUrl = uploadResult.get("secure_url").toString();
			String publicId = uploadResult.get("public_id").toString();

			return new CloudinaryUploadResponse(secureUrl, publicId);

		} catch (Exception e) {
			throw new RuntimeException("Cloudinary upload failed: " + e.getMessage());
		}
	}
}