package com.photoAI.business.repository;

import com.photoAI.business.repository.keys.PhotoTagId;
import com.photoAI.business.repository.model.PhotoTagDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoTagRepository extends JpaRepository<PhotoTagDAO, PhotoTagId> {
}