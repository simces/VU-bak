package com.photo.business.service.impl;

import com.photo.business.handlers.exceptions.*;
import com.photo.business.mappers.UserMapper;
import com.photo.business.repository.UserRepository;
import com.photo.business.repository.model.UserDAO;
import com.photo.business.service.UserService;
import com.photo.model.UserCreationDTO;
import com.photo.model.UserDTO;
import com.photo.model.UserPasswordChangeDTO;
import com.photo.model.UserProfileDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // returns user's profile
    @Override
    public UserProfileDTO findByUsername(String username) {
        UserDAO user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        return userMapper.userDAOToUserProfileDTO(user);
    }

    @Override
    public UserProfileDTO getUserById(Long id) {
        UserDAO user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
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
        newUser.setRole("ROLE_USER");

        if (newUser.getProfilePictureUrl() == null || newUser.getProfilePictureUrl().isEmpty()) {
            String defaultProfilePicUrl = "https://photo-ai-bak.s3.eu-north-1.amazonaws.com/4045d3b3-603e-4f4f-9fd5-a78aef4d06f8.png";
            newUser.setProfilePictureUrl(defaultProfilePicUrl);
        }

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
        if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmNewPassword())) {
            throw new PasswordConfirmationException("New Password and Confirm New Password do not match.");
        }

        UserDAO user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        if (!passwordEncoder.matches(passwordChangeDTO.getCurrentPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Current Password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDTO getCurrentUserProfileWithRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        UserDAO user = userRepository.findByUsername(currentUsername)
                .orElseThrow(UserNotFoundException::new);
        return userMapper.userDAOToUserDTO(user);
    }
}