package com.photo.business.repository;

import com.photo.business.repository.model.PhotoDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PhotoRepository extends JpaRepository<PhotoDAO, Long> {

    List<PhotoDAO> findByUserId(Long userId);

    Page<PhotoDAO> findByUserIdInOrderByUploadedAtDesc(List<Long> userIds, Pageable pageable);

}
