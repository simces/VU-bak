package com.photo.business.repository;

import com.photo.business.repository.model.CommentDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentDAO, Long> {
}
