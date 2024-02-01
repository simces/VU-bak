package com.photo.business.repository;

import com.photo.business.repository.model.PhotoDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<PhotoDAO, Long> {

    List<PhotoDAO> findByUserId(Long userId);
}
