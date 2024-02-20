package com.photo.controller;

import com.photo.business.service.PhotoService;
import com.photo.business.service.UserService;
import com.photo.model.PhotoDTO;
import com.photo.model.UserProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        return ResponseEntity.ok(response); // Return the data as JSON
    }
}

