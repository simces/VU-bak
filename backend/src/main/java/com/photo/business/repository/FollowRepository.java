package com.photo.business.repository;

import com.photo.business.repository.model.FollowDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<FollowDAO, FollowId> {
}
