package com.jalrakshak.jalrakshak.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.jalrakshak.jalrakshak.model.Role;
import com.jalrakshak.jalrakshak.model.User;
import com.jalrakshak.jalrakshak.repository.UserRepository;

@Component
public class AdminSeeder implements CommandLineRunner {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	@Value("${ADMIN_EMAIL}")
	private String adminEmail;

	@Value("${ADMIN_PASSWORD}")
	private String adminPassword;

	public AdminSeeder(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) {

		String email = adminEmail.toLowerCase();

		if (!userRepository.existsByEmail(email)) {

			User admin = new User();
			admin.setName("System Admin");
			admin.setEmail(email);
			admin.setMobileNumber("9999999999");
			admin.setPasswordHash(passwordEncoder.encode(adminPassword));
			admin.setRole(Role.ADMIN);
			admin.setEmailVerified(true);

			userRepository.save(admin);

			System.out.println("Admin created successfully: " + email);
		}
	}
}