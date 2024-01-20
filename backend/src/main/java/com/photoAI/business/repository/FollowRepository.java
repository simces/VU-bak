package com.photoAI.business.repository;

import com.photoAI.business.repository.keys.FollowId;
import com.photoAI.business.repository.model.FollowDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<FollowDAO, FollowId> {
}
