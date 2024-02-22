package com.photo.business.repository;

import com.photo.business.repository.model.CommentDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentDAO, Long> {

    List<CommentDAO> findByPhotoId(Long photoId);

}
