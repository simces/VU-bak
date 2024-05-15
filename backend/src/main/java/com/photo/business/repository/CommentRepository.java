package com.photo.business.repository;

import com.photo.business.repository.model.CommentDAO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentDAO, Long> {

    @EntityGraph(attributePaths = {"user", "photo"})
    List<CommentDAO> findByPhotoId(Long photoId);

    @EntityGraph(attributePaths = {"user", "photo"})
    Optional<CommentDAO> findById(Long id);

    void deleteByPhotoId(Long photoId);
}

