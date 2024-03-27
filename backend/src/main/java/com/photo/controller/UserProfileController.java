package com.photo.controller;

import com.photo.business.handlers.exceptions.IncorrectPasswordException;
import com.photo.business.handlers.exceptions.PasswordConfirmationException;
import com.photo.business.service.PhotoService;
import com.photo.business.service.UserService;
import com.photo.model.PhotoDTO;
import com.photo.model.UserPasswordChangeDTO;
import com.photo.model.UserProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<?> userProfile(@PathVariable String username) {
        UserProfileDTO userProfile = userService.findByUsername(username);
        if (userProfile == null) {
            return ResponseEntity.notFound().build(); // Return 404 if user not found
        }
        List<PhotoDTO> photos = photoService.getPhotosByUserId(userProfile.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("userProfile", userProfile);
        response.put("photos", photos);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getCurrentUserProfile() {
        UserProfileDTO userProfile = userService.getCurrentUserProfile();
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@Validated @RequestBody UserProfileDTO userProfileDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        UserProfileDTO currentUserProfile = userService.findByUsername(currentUsername);
        if (currentUserProfile == null) {
            return ResponseEntity.notFound().build();
        }

        UserProfileDTO updatedUserProfile = userService.changeProfileDetails(currentUserProfile.getId(), userProfileDTO);
        return ResponseEntity.ok(updatedUserProfile);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Validated @RequestBody UserPasswordChangeDTO passwordChangeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        UserProfileDTO currentUserProfile = userService.findByUsername(currentUsername);
        if (currentUserProfile == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            userService.changePassword(currentUserProfile.getId(), passwordChangeDTO);
            return ResponseEntity.ok().build();
        } catch (PasswordConfirmationException | IncorrectPasswordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

