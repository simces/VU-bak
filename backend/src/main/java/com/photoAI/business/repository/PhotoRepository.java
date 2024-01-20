package com.photoAI.business.repository;

import com.photoAI.business.repository.model.PhotoDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<PhotoDAO, Long> {
}
