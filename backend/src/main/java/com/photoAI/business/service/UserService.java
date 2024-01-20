package com.photoAI.business.service;

import com.photoAI.business.repository.model.UserDAO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDAO createUser(UserDAO user);

    Optional<UserDAO> getUserById(Long id);

    List<UserDAO> getAllUsers();

    UserDAO updateUser(Long id, UserDAO userDetails);

    void deleteUser(Long id);

    // Additional methods based on your business logic (e.g., findByUsername, changePassword, etc.)
}
