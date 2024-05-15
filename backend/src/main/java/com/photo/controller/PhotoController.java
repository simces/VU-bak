package com.photo.controller;

import com.photo.business.service.PhotoService;
import com.photo.business.service.TagService;
import com.photo.business.service.UserService;
import com.photo.model.photos.FullPhotoDTO;
import com.photo.model.photos.PhotoDTO;
import com.photo.model.photos.PhotoResponseDTO;
import com.photo.model.users.UserBasicDetailsDTO;
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
            PhotoResponseDTO photoResponseDTO = photoService.getPhotoResponseById(photoId);
            return ResponseEntity.ok(photoResponseDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/full/{photoId}")
    public ResponseEntity<FullPhotoDTO> getFullPhotoById(@PathVariable Long photoId) {
        try {
            FullPhotoDTO fullPhotoDTO = photoService.getFullPhotoById(photoId);
            return ResponseEntity.ok(fullPhotoDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

