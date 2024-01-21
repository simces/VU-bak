package com.photo.business.repository;

import com.photo.business.repository.model.NotificationDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationDAO, Long> {
}
