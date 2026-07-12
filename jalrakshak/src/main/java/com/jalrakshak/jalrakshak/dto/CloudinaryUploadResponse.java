package com.jalrakshak.jalrakshak.dto;

public class CloudinaryUploadResponse {

	private String photoUrl;
	private String publicId;

	public CloudinaryUploadResponse() {

	}

	public CloudinaryUploadResponse(String photoUrl, String publicId) {
		this.photoUrl = photoUrl;
		this.publicId = publicId;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

}
