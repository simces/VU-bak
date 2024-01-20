package com.photoAI.business.repository;

import com.photoAI.business.repository.model.NotificationDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationDAO, Long> {
}
