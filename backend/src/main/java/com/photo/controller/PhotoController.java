package com.photo.controller;

import com.photo.business.service.PhotoService;
import com.photo.business.service.TagService;
import com.photo.business.service.UserService;
import com.photo.model.PhotoDTO;
import com.photo.model.PhotoResponseDTO;
import com.photo.model.TagDTO;
import com.photo.model.UserProfileDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/photos")
@CrossOrigin(origins = "http://localhost:3000") // react app origin url
public class PhotoController {

    private final PhotoService photoService;
    private final UserService userService;
    private final TagService tagService;

    public PhotoController(PhotoService photoService, UserService userService, TagService tagService) {
        this.photoService = photoService;
        this.userService = userService;
        this.tagService = tagService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file,
                                              @RequestParam("title") String title,
                                              @RequestParam("description") String description) {
        try {
            PhotoDTO photoDTO = new PhotoDTO();
            photoDTO.setTitle(title);
            photoDTO.setDescription(description);
            photoService.uploadPhotoFile(photoDTO, file);
            return ResponseEntity.ok().body("Photo uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoResponseDTO> getPhotoById(@PathVariable Long photoId) {
        try {
            PhotoDTO photoDTO = photoService.getPhotoById(photoId);
            UserProfileDTO userProfileDTO = userService.getUserById(photoDTO.getUserId());
            TagDTO tagDTO = tagService.getTagByPhotoId(photoId);

            PhotoResponseDTO photoResponseDTO = new PhotoResponseDTO(photoDTO, userProfileDTO, tagDTO);

            return ResponseEntity.ok(photoResponseDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

