package com.photoAI.business.repository;

import com.photoAI.business.repository.model.LikeDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeDAO, Long> {
}
