package com.jalrakshak.jalrakshak.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jalrakshak.jalrakshak.dto.AuthResponse;
import com.jalrakshak.jalrakshak.dto.ForgotPasswordRequest;
import com.jalrakshak.jalrakshak.dto.LoginRequest;
import com.jalrakshak.jalrakshak.dto.RegisterRequest;
import com.jalrakshak.jalrakshak.dto.ResetPasswordRequest;
import com.jalrakshak.jalrakshak.dto.VerifyEmailRequest;
import com.jalrakshak.jalrakshak.model.Role;
import com.jalrakshak.jalrakshak.model.User;
import com.jalrakshak.jalrakshak.model.Village;
import com.jalrakshak.jalrakshak.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final VillageService villageService;
	private final EmailService emailService;

	private final SecureRandom secureRandom = new SecureRandom();

	public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
			VillageService villageService, EmailService emailService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.villageService = villageService;
		this.emailService = emailService;
	}

	public AuthResponse register(RegisterRequest request) {

		String email = request.getEmail().toLowerCase();

		if (userRepository.existsByEmail(email)) {
			throw new RuntimeException("Email already registered");
		}

		String otp = generateOtp();

		User user = new User();
		user.setName(request.getName());
		user.setEmail(email);
		user.setMobileNumber(request.getMobileNumber());
		user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
		user.setRole(request.getRole());
		user.setEmailVerified(false);
		user.setEmailOtp(otp);
		user.setEmailOtpExpiry(LocalDateTime.now().plusMinutes(10));

		if (request.getVillageId() != null) {
			Village village = villageService.getVillageById(request.getVillageId());
			user.setVillage(village);
		}

		User savedUser = userRepository.save(user);

		emailService.sendSimpleEmail(savedUser.getEmail(), "JalRakshak Email Verification OTP",
				"Hello " + savedUser.getName() + ",\n\n" + "Your JalRakshak verification OTP is: " + otp + "\n"
						+ "This OTP is valid for 10 minutes.\n\n" + "Thank you,\nJalRakshak Team");

		return new AuthResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(),
				savedUser.getMobileNumber(), savedUser.getRole(),
				"Registration successful. OTP sent to your email for verification.");
	}

	public String verifyEmail(VerifyEmailRequest request) {

		String email = request.getEmail().toLowerCase();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		if (Boolean.TRUE.equals(user.getEmailVerified())) {
			return "Email already verified";
		}

		if (user.getEmailOtp() == null || user.getEmailOtpExpiry() == null) {
			throw new RuntimeException("OTP not found. Please request OTP again.");
		}

		if (LocalDateTime.now().isAfter(user.getEmailOtpExpiry())) {
			throw new RuntimeException("OTP expired. Please request OTP again.");
		}

		if (!user.getEmailOtp().equals(request.getOtp())) {
			throw new RuntimeException("Invalid OTP");
		}

		user.setEmailVerified(true);
		user.setEmailOtp(null);
		user.setEmailOtpExpiry(null);

		userRepository.save(user);

		return "Email verified successfully";
	}

	public String resendEmailOtp(String email) {

		String cleanEmail = email.toLowerCase();

		User user = userRepository.findByEmail(cleanEmail).orElseThrow(() -> new RuntimeException("User not found"));

		if (Boolean.TRUE.equals(user.getEmailVerified())) {
			return "Email already verified";
		}

		String otp = generateOtp();

		user.setEmailOtp(otp);
		user.setEmailOtpExpiry(LocalDateTime.now().plusMinutes(10));

		userRepository.save(user);

		emailService.sendSimpleEmail(user.getEmail(), "JalRakshak New Verification OTP",
				"Hello " + user.getName() + ",\n\n" + "Your new verification OTP is: " + otp + "\n"
						+ "This OTP is valid for 10 minutes.\n\n" + "Thank you,\nJalRakshak Team");

		return "New OTP sent to email";
	}

	public AuthResponse login(LoginRequest request) {

		String email = request.getEmail().toLowerCase();

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Invalid email or password"));

		boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

		if (!passwordMatches) {
			throw new RuntimeException("Invalid email or password");
		}

		if (!Boolean.TRUE.equals(user.getEmailVerified())) {
			throw new RuntimeException("Please verify your email before login");
		}

		return new AuthResponse(user.getId(), user.getName(), user.getEmail(), user.getMobileNumber(), user.getRole(),
				"Login successful");
	}

	public String forgotPassword(ForgotPasswordRequest request) {

		String email = request.getEmail().toLowerCase();

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with this email"));

		String otp = generateOtp();

		user.setResetOtp(otp);
		user.setResetOtpExpiry(LocalDateTime.now().plusMinutes(10));

		userRepository.save(user);

		emailService.sendSimpleEmail(user.getEmail(), "JalRakshak Password Reset OTP",
				"Hello " + user.getName() + ",\n\n" + "Your password reset OTP is: " + otp + "\n"
						+ "This OTP is valid for 10 minutes.\n\n"
						+ "If you did not request this, please ignore this email.\n\n" + "Thank you,\nJalRakshak Team");

		return "Password reset OTP sent to your email";
	}

	public String resetPassword(ResetPasswordRequest request) {

		String email = request.getEmail().toLowerCase();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		if (user.getResetOtp() == null || user.getResetOtpExpiry() == null) {
			throw new RuntimeException("Reset OTP not found. Please request forgot password again.");
		}

		if (LocalDateTime.now().isAfter(user.getResetOtpExpiry())) {
			throw new RuntimeException("Reset OTP expired. Please request again.");
		}

		if (!user.getResetOtp().equals(request.getOtp())) {
			throw new RuntimeException("Invalid reset OTP");
		}

		user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
		user.setResetOtp(null);
		user.setResetOtpExpiry(null);

		userRepository.save(user);

		emailService.sendSimpleEmail(user.getEmail(), "JalRakshak Password Changed", "Hello " + user.getName() + ",\n\n"
				+ "Your JalRakshak password has been changed successfully.\n\n" + "Thank you,\nJalRakshak Team");

		return "Password reset successfully";
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public List<User> getUsersByRole(Role role) {
		return userRepository.findByRole(role);
	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
	}

	public List<User> getUsersByVillage(Long villageId) {
		return userRepository.findByVillage_Id(villageId);
	}

	private String generateOtp() {
		int otp = 100000 + secureRandom.nextInt(900000);
		return String.valueOf(otp);
	}
}