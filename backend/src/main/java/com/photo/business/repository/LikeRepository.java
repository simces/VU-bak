package com.photo.business.repository;

import com.photo.business.repository.model.LikeDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeDAO, Long> {
}
