package com.photo.business.repository;

import com.photo.business.repository.model.LikeDAO;
import com.photo.business.repository.model.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeDAO, Long> {

    Optional<LikeDAO> findByPhotoIdAndUserId(Long photoId, Long userId);

    @Query("SELECT l.user FROM LikeDAO l WHERE l.photo.id = :photoId")
    List<UserDAO> findUsersByLikedPhoto(@Param("photoId") Long photoId);

    void deleteByPhotoId(Long photoId);
}