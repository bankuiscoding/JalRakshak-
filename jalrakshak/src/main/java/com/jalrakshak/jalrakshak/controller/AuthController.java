package com.jalrakshak.jalrakshak.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jalrakshak.jalrakshak.dto.AuthResponse;
import com.jalrakshak.jalrakshak.dto.ForgotPasswordRequest;
import com.jalrakshak.jalrakshak.dto.LoginRequest;
import com.jalrakshak.jalrakshak.dto.RegisterRequest;
import com.jalrakshak.jalrakshak.dto.ResetPasswordRequest;
import com.jalrakshak.jalrakshak.dto.VerifyEmailRequest;
import com.jalrakshak.jalrakshak.model.Role;
import com.jalrakshak.jalrakshak.model.User;
import com.jalrakshak.jalrakshak.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
		return userService.register(request);
	}

	@PostMapping("/verify-email")
	public String verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
		return userService.verifyEmail(request);
	}

	@PostMapping("/resend-otp")
	public String resendOtp(@RequestParam String email) {
		return userService.resendEmailOtp(email);
	}

	@PostMapping("/login")
	public AuthResponse login(@Valid @RequestBody LoginRequest request) {
		return userService.login(request);
	}

	@PostMapping("/forgot-password")
	public String forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
		return userService.forgotPassword(request);
	}

	@PostMapping("/reset-password")
	public String resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
		return userService.resetPassword(request);
	}

	@GetMapping("/users")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/users/role/{role}")
	public List<User> getUsersByRole(@PathVariable Role role) {
		return userService.getUsersByRole(role);
	}

	@GetMapping("/users/{id}")
	public User getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
	}
}