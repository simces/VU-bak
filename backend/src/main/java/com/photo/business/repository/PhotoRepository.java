package com.photo.business.repository;

import com.photo.business.repository.model.PhotoDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<PhotoDAO, Long> {
}
