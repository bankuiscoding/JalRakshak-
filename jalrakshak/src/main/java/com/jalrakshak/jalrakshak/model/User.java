package com.jalrakshak.jalrakshak.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	private String mobileNumber;

	private String passwordHash;

	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private Role role;

	@ManyToOne
	@JoinColumn(name = "village_id")
	private Village village;

	private Boolean emailVerified;

	private String emailOtp;

	private LocalDateTime emailOtpExpiry;

	private String resetOtp;

	private LocalDateTime resetOtpExpiry;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public User() {
	}

	@PrePersist
	public void beforeSave() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
		if (this.emailVerified == null) {
			this.emailVerified = false;
		}
	}

	@PreUpdate
	public void beforeUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public Role getRole() {
		return role;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Village getVillage() {
		return village;
	}

	public void setVillage(Village village) {
		this.village = village;
	}

	public Boolean getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(Boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public String getEmailOtp() {
		return emailOtp;
	}

	public void setEmailOtp(String emailOtp) {
		this.emailOtp = emailOtp;
	}

	public LocalDateTime getEmailOtpExpiry() {
		return emailOtpExpiry;
	}

	public void setEmailOtpExpiry(LocalDateTime emailOtpExpiry) {
		this.emailOtpExpiry = emailOtpExpiry;
	}

	public String getResetOtp() {
		return resetOtp;
	}

	public void setResetOtp(String resetOtp) {
		this.resetOtp = resetOtp;
	}

	public LocalDateTime getResetOtpExpiry() {
		return resetOtpExpiry;
	}

	public void setResetOtpExpiry(LocalDateTime resetOtpExpiry) {
		this.resetOtpExpiry = resetOtpExpiry;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}