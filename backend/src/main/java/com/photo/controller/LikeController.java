package com.photo.controller;

import com.photo.business.repository.model.LikeDAO;
import com.photo.business.service.LikeService;
import com.photo.model.LikeDetailDTO;
import com.photo.model.LikeStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/photos")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // Endpoint to like a photo
    @PostMapping("/{photoId}/likes")
    public ResponseEntity<LikeDAO> addLike(@PathVariable Long photoId) {
        LikeDAO savedLike = likeService.likePhoto(photoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLike);
    }

    // Endpoint to unlike a photo
    @DeleteMapping("/likes/{likeId}")
    public ResponseEntity<Void> unlikePhoto(@PathVariable Long likeId) {
        likeService.unlikePhoto(likeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test/{photoId}")
    public ResponseEntity<LikeStatusDTO> checkPhotoLikeStatus(@PathVariable Long photoId) {
        LikeStatusDTO likeStatus = likeService.checkLikeStatus(photoId);
        return ResponseEntity.ok(likeStatus);
    }

    @GetMapping("/{photoId}/likelist")
    public ResponseEntity<LikeDetailDTO> getPhotoLikes(@PathVariable Long photoId) {
        LikeDetailDTO likeDetails = likeService.getLikeDetails(photoId);
        return ResponseEntity.ok(likeDetails);
    }
}

