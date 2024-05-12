package com.photo.business.repository;

import com.photo.business.repository.model.AuditDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditRepository extends JpaRepository<AuditDAO, Long> {

    List<AuditDAO> findAllByOrderByCreatedAtDesc();
}
