package com.photo.business.service.impl;

import com.photo.business.handlers.exceptions.EmailAlreadyInUseException;
import com.photo.business.handlers.exceptions.IncorrectPasswordException;
import com.photo.business.handlers.exceptions.UserNotFoundException;
import com.photo.business.handlers.exceptions.UsernameAlreadyTakenException;
import com.photo.business.mappers.UserMapper;
import com.photo.business.repository.UserRepository;
import com.photo.business.repository.model.UserDAO;
import com.photo.business.service.UserService;
import com.photo.model.UserCreationDTO;
import com.photo.model.UserPasswordChangeDTO;
import com.photo.model.UserProfileDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserProfileDTO findByUsername(String username) {
        UserDAO user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        return userMapper.userDAOToUserProfileDTO(user);
    }

    @Transactional
    @Override
    public UserDAO registerUser(UserCreationDTO userCreationDTO) {
        if (userRepository.existsByUsername(userCreationDTO.getUsername())) {
            throw new UsernameAlreadyTakenException();
        }
        if (userRepository.existsByEmail(userCreationDTO.getEmail())) {
            throw new EmailAlreadyInUseException("The email " + userCreationDTO.getEmail() + " is already registered.");
        }

        UserDAO newUser = userMapper.userCreationDTOToUserDAO(userCreationDTO);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        Timestamp now = Timestamp.from(Instant.now());
        newUser.setCreatedAt(now);
        newUser.setUpdatedAt(now);

        return userRepository.save(newUser);
    }

    @Transactional
    @Override
    public UserProfileDTO changeProfileDetails(Long userId, UserProfileDTO userProfileDTO) {
        UserDAO user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        user.setUsername(userProfileDTO.getUsername());
        user.setEmail(userProfileDTO.getEmail());
        user.setBio(userProfileDTO.getBio());
        user.setProfilePictureUrl(userProfileDTO.getProfilePictureUrl());
        user.setUpdatedAt(Timestamp.from(Instant.now()));

        userRepository.save(user);

        return userMapper.userDAOToUserProfileDTO(user);
    }

    @Transactional
    @Override
    public void changePassword(Long userId, UserPasswordChangeDTO passwordChangeDTO) {
        UserDAO user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(passwordChangeDTO.getCurrentPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }

        user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));

        userRepository.save(user);
    }
}