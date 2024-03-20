package com.photo.business.repository;

import com.photo.business.repository.model.LikeDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeDAO, Long> {

    Optional<LikeDAO> findByPhotoIdAndUserId(Long photoId, Long userId);

}
