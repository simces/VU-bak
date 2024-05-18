package com.photo.business.repository;

import com.photo.business.repository.keys.PhotoTagId;
import com.photo.business.repository.model.CategoryDAO;
import com.photo.business.repository.model.PhotoTagDAO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoTagRepository extends JpaRepository<PhotoTagDAO, PhotoTagId> {

    List<PhotoTagDAO> findByPhotoId(Long photoId);

    void deleteByPhotoId(Long photoId);

    int countByCategory(CategoryDAO category);

    List<PhotoTagDAO> findByCategoryIdIn(List<Long> categoryIds, Pageable pageable);
}