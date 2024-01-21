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
        return userRepository.save(newUser);
    }


    @Transactional
    @Override
    public UserProfileDTO changeProfileDetails(Long userId, UserProfileDTO userProfileDTO) {
        UserDAO user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDAO updatedUser = userMapper.userProfileDTOToUserDAO(userProfileDTO);
        updatedUser.setId(user.getId());

        UserDAO savedUser = userRepository.save(updatedUser);
        return userMapper.userDAOToUserProfileDTO(savedUser);
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