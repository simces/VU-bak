package com.photo.controller;

import com.photo.business.service.impl.LoginService;
import com.photo.model.AuthResponseDTO;
import com.photo.model.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

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
}
