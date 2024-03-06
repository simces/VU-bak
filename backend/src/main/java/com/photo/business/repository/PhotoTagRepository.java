package com.photo.business.repository;

import com.photo.business.repository.keys.PhotoTagId;
import com.photo.business.repository.model.PhotoTagDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface PhotoTagRepository extends JpaRepository<PhotoTagDAO, PhotoTagId> {
    boolean existsByPhotoIdAndTagId(Long photoId, Long tagId);
}