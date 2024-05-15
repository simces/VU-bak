package com.photo.controller;

import com.photo.business.service.LikeService;
import com.photo.model.likes.LikeDTO;
import com.photo.model.likes.LikeDetailDTO;
import com.photo.model.likes.LikeStatusDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public ResponseEntity<LikeDTO> addLike(@PathVariable Long photoId) {
        try {
            LikeDTO savedLike = likeService.likePhoto(photoId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLike);
        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint to unlike a photo

    @DeleteMapping("/likes/{likeId}")
    public ResponseEntity<Void> unlikePhoto(@PathVariable Long likeId) {
        try {
            likeService.unlikePhoto(likeId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

