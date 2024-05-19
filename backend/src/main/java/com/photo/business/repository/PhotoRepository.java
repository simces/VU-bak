package com.photo.business.repository;

import com.photo.business.repository.model.PhotoDAO;
import com.photo.model.photos.PhotoRankDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PhotoRepository extends JpaRepository<PhotoDAO, Long> {

    List<PhotoDAO> findByUserId(Long userId);

    Page<PhotoDAO> findByUserIdInOrderByUploadedAtDesc(List<Long> userIds, Pageable pageable);

    @Query("SELECT p FROM PhotoDAO p WHERE p.uploadedAt >= :cutoffTime")
    List<PhotoDAO> findRecentPhotos(@Param("cutoffTime") LocalDateTime cutoffTime);

    @NotNull
    Page<PhotoDAO> findAll(@NotNull Pageable pageable);

    Page<PhotoDAO> findAllByOrderByUploadedAtDesc(Pageable pageable);
}
