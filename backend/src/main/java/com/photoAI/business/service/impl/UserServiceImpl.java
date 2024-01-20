package com.photoAI.business.service.impl;

import com.photoAI.business.repository.model.UserDAO;
import com.photoAI.business.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserDAO createUser(UserDAO user) {
        return null;
    }

    @Override
    public Optional<UserDAO> getUserById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<UserDAO> getAllUsers() {
        return null;
    }

    @Override
    public UserDAO updateUser(Long id, UserDAO userDetails) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }
}