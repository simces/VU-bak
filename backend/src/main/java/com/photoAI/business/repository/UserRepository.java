package com.photoAI.business.repository;

import com.photoAI.business.repository.model.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDAO, Long> {
}
