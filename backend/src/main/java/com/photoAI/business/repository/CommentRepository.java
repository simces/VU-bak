package com.photoAI.business.repository;

import com.photoAI.business.repository.model.CommentDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentDAO, Long> {
}
