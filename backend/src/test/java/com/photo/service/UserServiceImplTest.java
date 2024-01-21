package com.photo.service;

import com.photo.business.mappers.UserMapper;
import com.photo.business.repository.UserRepository;
import com.photo.business.repository.model.UserDAO;
import com.photo.business.service.impl.UserServiceImpl;
import com.photo.model.UserCreationDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void whenRegisterWithValidUser_thenSavesUser() {
        UserCreationDTO validUserDTO = new UserCreationDTO("DoingTesting123", "DoingTesting@example.com", "Password1234");
        UserDAO newUserDAO = new UserDAO();
        newUserDAO.setUsername(validUserDTO.getUsername());
        newUserDAO.setEmail(validUserDTO.getEmail());
        newUserDAO.setPassword(validUserDTO.getPassword());

        when(userRepository.existsByUsername(validUserDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(validUserDTO.getEmail())).thenReturn(false);
        when(userMapper.userCreationDTOToUserDAO(validUserDTO)).thenReturn(newUserDAO);
        when(passwordEncoder.encode(validUserDTO.getPassword())).thenReturn("Password1234");
        when(userRepository.save(any(UserDAO.class))).thenReturn(newUserDAO);

        UserDAO savedUser = userService.registerUser(validUserDTO);

        assertNotNull(savedUser);
        assertEquals("DoingTesting123", savedUser.getUsername());
        verify(userRepository).save(any(UserDAO.class));
    }


    @Test
    public void whenRegisterWithTakenUsername_thenThrowsException() {
        UserCreationDTO userWithTakenUsername = new UserCreationDTO("slaptazodisYra123", "heyayays@hello.hi", "123");

        when(userRepository.existsByUsername(userWithTakenUsername.getUsername())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(userWithTakenUsername));
    }

    @Test
    public void whenRegisterWithEmailAlreadyInUse_thenThrowsException() {
        UserCreationDTO userWithEmailInUse = new UserCreationDTO("slaptsYra123", "hey@hello.hi", "123");

        when(userRepository.existsByUsername(userWithEmailInUse.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userWithEmailInUse.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(userWithEmailInUse));
    }
}

