package com.photo.business.repository;

import com.photo.business.repository.model.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserDAO, Long> {
    Optional<UserDAO> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
