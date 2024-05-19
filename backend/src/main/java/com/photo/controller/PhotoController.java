package com.photo.controller;

import com.photo.business.service.PhotoService;
import com.photo.model.photos.FullPhotoDTO;
import com.photo.model.photos.HotPhotoDTO;
import com.photo.model.photos.PhotoDTO;
import com.photo.model.photos.PhotoResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/photos")
@CrossOrigin(origins = "http://localhost:3000") // react app origin url
public class PhotoController {

    private final PhotoService photoService;

    private static final Logger logger = LogManager.getLogger(PhotoController.class);

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPhoto(@RequestParam("title") String title,
                                         @RequestParam("description") String description,
                                         @RequestParam("file") MultipartFile file,
                                         @RequestParam(value = "deviceId", required = false) Long deviceId,
                                         @RequestParam(value = "latitude", required = false) Double latitude,
                                         @RequestParam(value = "longitude", required = false) Double longitude) {
        try {
            logger.info("Received upload request: title={}, description={}, deviceId={}, latitude={}, longitude={}",
                    title, description, deviceId, latitude, longitude);

            PhotoDTO photoDTO = new PhotoDTO();
            photoDTO.setTitle(title);
            photoDTO.setDescription(description);
            photoDTO.setDeviceId(deviceId);
            photoDTO.setLatitude(latitude);
            photoDTO.setLongitude(longitude);
            photoService.uploadPhotoFile(photoDTO, file);
            return ResponseEntity.ok("Photo uploaded successfully");
        } catch (Exception e) {
            logger.error("Error during photo upload", e);
            return ResponseEntity.badRequest().body(e.getMessage());
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

    @GetMapping("/hot")
    public List<HotPhotoDTO> getHotPhotos(@RequestParam int page, @RequestParam int size) {
        return photoService.getHotPhotos(page, size);
    }
}

