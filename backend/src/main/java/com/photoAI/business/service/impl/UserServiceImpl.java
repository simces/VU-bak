package com.photoAI.business.service.impl;

import com.photoAI.business.mappers.UserMapper;
import com.photoAI.business.repository.UserRepository;
import com.photoAI.business.repository.model.UserDAO;
import com.photoAI.business.service.UserService;
import com.photoAI.model.UserCreationDTO;
import com.photoAI.model.UserPasswordChangeDTO;
import com.photoAI.model.UserProfileDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userMapper.userDAOToUserProfileDTO(user);
    }


    @Transactional
    @Override
    public UserDAO registerUser(UserCreationDTO userCreationDTO) {
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
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

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
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(passwordChangeDTO.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect current password");
        }

        user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));

        userRepository.save(user);
    }
}