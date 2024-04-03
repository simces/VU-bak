package com.photo.business.repository;

import com.photo.business.repository.model.FollowDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<FollowDAO, Long> {

    Optional<FollowDAO> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query("SELECT COUNT(f) FROM FollowDAO f WHERE f.followingId = :userId")
    long countFollowersOfUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(f) FROM FollowDAO f WHERE f.followerId = :userId")
    long countFollowingsOfUser(@Param("userId") Long userId);

    @Query("SELECT f FROM FollowDAO f WHERE f.followingId = :userId")
    List<FollowDAO> findFollowersOfUser(@Param("userId") Long userId);
    // need to change to return user list, rather than id list/ also maybe first 100?
    @Query("SELECT f FROM FollowDAO f WHERE f.followerId = :userId")
    List<FollowDAO> findFollowingsOfUser(@Param("userId") Long userId);
    // need to change to return user list, rather than id list
}
