package com.jalrakshak.jalrakshak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jalrakshak.jalrakshak.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByUser_IdOrderByCreatedAtDesc(Long userId);

	List<Notification> findByUser_IdAndReadStatusFalseOrderByCreatedAtDesc(Long userId);
}