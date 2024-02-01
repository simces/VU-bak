package com.photo.controller;

import com.photo.business.service.PhotoService;
import com.photo.model.PhotoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/userprofile")
public class UserProfileController {

    @Autowired
    private PhotoService photoService;

    @GetMapping("/{userId}")
    public String viewUserProfile(@PathVariable Long userId, Model model) {
        List<PhotoDTO> photos = photoService.getPhotosByUserId(userId);
        model.addAttribute("photos", photos);
        return "user-profile";
    }
}
