package com.photo.controller;

import com.photo.business.repository.model.UserDAO;
import com.photo.business.service.UserService;
import com.photo.model.UserCreationDTO;
import com.photo.model.UserPasswordChangeDTO;
import com.photo.model.UserProfileDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET user by username
    @GetMapping("/{username}")
    public ResponseEntity<UserProfileDTO> findByUsername(@PathVariable String username) {
        UserProfileDTO user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    /*
    //POST - Register a new user, but for Postman requests
    @PostMapping("/register")
    public ResponseEntity<UserDAO> registerUser(@Valid @RequestBody UserCreationDTO userCreationDTO) {
        UserDAO newUser = userService.registerUser(userCreationDTO);
        return ResponseEntity.ok(newUser);
    }
    */

    // PUT - Update user profile details
    @PutMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDTO> changeProfileDetails(@PathVariable Long userId, @RequestBody UserProfileDTO userProfileDTO) {
        UserProfileDTO updatedUser = userService.changeProfileDetails(userId, userProfileDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // PUT - Change user password
    @PutMapping("/{userId}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long userId, @Valid @RequestBody UserPasswordChangeDTO passwordChangeDTO) {
        userService.changePassword(userId, passwordChangeDTO);
        return ResponseEntity.ok().build();
    }
}
