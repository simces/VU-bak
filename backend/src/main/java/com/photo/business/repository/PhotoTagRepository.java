package com.photo.business.repository;

import com.photo.business.repository.keys.PhotoTagId;
import com.photo.business.repository.model.PhotoTagDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PhotoTagRepository extends JpaRepository<PhotoTagDAO, PhotoTagId> {

    List<PhotoTagDAO> findByPhotoId(Long photoId);

    void deleteByPhotoId(Long photoId);
}