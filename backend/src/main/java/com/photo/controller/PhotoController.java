package com.photo.controller;

import com.photo.business.service.PhotoService;
import com.photo.model.PhotoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/photos")
public class PhotoController {

    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        model.addAttribute("photoDTO", new PhotoDTO());
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @ModelAttribute PhotoDTO photoDTO,
                                   RedirectAttributes redirectAttributes) {
        try {
            // You will need to update the service to handle MultipartFile
            // and save the file to your desired location (like AWS S3)
            photoService.uploadPhotoFile(photoDTO, file);
            redirectAttributes.addFlashAttribute("message", "Photo uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Upload failed: " + e.getMessage());
        }
        return "redirect:/photos/upload";
    }
}