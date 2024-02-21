package com.photo.controller;

import com.photo.business.handlers.exceptions.EmailAlreadyInUseException;
import com.photo.business.handlers.exceptions.UsernameAlreadyTakenException;
import com.photo.business.service.UserService;
import com.photo.business.service.impl.LoginService;
import com.photo.model.AuthResponseDTO;
import com.photo.model.LoginDTO;
import com.photo.model.UserCreationDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.ObjectError;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    private final UserService userService;

    public LoginController(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            String token = loginService.authenticateAndGetToken(loginDTO.getUsername(), loginDTO.getPassword());
            return ResponseEntity.ok(new AuthResponseDTO(token));
        } catch (AuthenticationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Authentication failed");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserCreationDTO userCreationDTO,
                                          BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            userService.registerUser(userCreationDTO);
            return ResponseEntity.ok().body("User registered successfully");
        } catch (UsernameAlreadyTakenException | EmailAlreadyInUseException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
