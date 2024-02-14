package com.photo.controller;

import com.photo.business.service.PhotoService;
import com.photo.business.service.UserService;
import com.photo.model.PhotoDTO;
import com.photo.model.UserProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/users")
public class UserProfileController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    public String userProfile(@PathVariable String username, Model model) {
        UserProfileDTO userProfile = userService.findByUsername(username);
        List<PhotoDTO> photos = photoService.getPhotosByUserId(userProfile.getId());
        model.addAttribute("userProfile", userProfile);
        model.addAttribute("photos", photos);
        return "user-profile";
    }
}
