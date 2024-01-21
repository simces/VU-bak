package com.photoAI.controller;

import com.photoAI.business.repository.model.UserDAO;
import com.photoAI.business.service.UserService;
import com.photoAI.model.UserCreationDTO;
import com.photoAI.model.UserPasswordChangeDTO;
import com.photoAI.model.UserProfileDTO;
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

    // POST - Register a new user
    @PostMapping("/register")
    public ResponseEntity<UserDAO> registerUser(@RequestBody UserCreationDTO userCreationDTO) {
        UserDAO newUser = userService.registerUser(userCreationDTO);
        return ResponseEntity.ok(newUser); // Consider using ResponseEntity.created() with the location of the new user
    }

    // PUT - Update user profile details
    @PutMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDTO> changeProfileDetails(@PathVariable Long userId, @RequestBody UserProfileDTO userProfileDTO) {
        UserProfileDTO updatedUser = userService.changeProfileDetails(userId, userProfileDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // PUT - Change user password
    @PutMapping("/{userId}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long userId, @RequestBody UserPasswordChangeDTO passwordChangeDTO) {
        userService.changePassword(userId, passwordChangeDTO);
        return ResponseEntity.ok().build();
    }
}
