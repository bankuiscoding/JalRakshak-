package com.jalrakshak.jalrakshak.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

	@Value("${cloudinary.cloud-name}")
	private String cloudName;

	@Value("${cloudinary.api-key}")
	private String apiKey;

	@Value("${cloudinary.api-secret}")
	private String apiSecret;

	@Bean
	public Cloudinary cloudinary() {

		String cleanCloudName = cloudName.trim();
		String cleanApiKey = apiKey.trim();
		String cleanApiSecret = apiSecret.trim();

		System.out.println("Cloudinary Cloud Name = " + cleanCloudName);
		System.out.println("Cloudinary API Key = " + cleanApiKey);
		System.out.println("Cloudinary API Secret Length = " + cleanApiSecret.length());
		System.out.println("Cloudinary API Secret First 2 = " + cleanApiSecret.substring(0, 2));
		System.out.println("Cloudinary API Secret Last 2 = " + cleanApiSecret.substring(cleanApiSecret.length() - 2));

		Map<String, String> config = ObjectUtils.asMap("cloud_name", cleanCloudName, "api_key", cleanApiKey,
				"api_secret", cleanApiSecret, "secure", "true");

		return new Cloudinary(config);
	}
}