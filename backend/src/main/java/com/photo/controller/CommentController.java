package com.photo.controller;

import com.photo.business.repository.model.CommentDAO;
import com.photo.business.service.CommentService;
import com.photo.model.comments.CommentDTO;
import com.photo.model.comments.CommentDetailDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photos/{photoId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDetailDTO> addComment(@PathVariable Long photoId, @RequestBody CommentDTO commentDTO) {
        commentDTO.setPhotoId(photoId);  // Set the photo ID from the path variable
        CommentDetailDTO savedComment = commentService.addCommentToPhoto(commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @GetMapping
    public ResponseEntity<List<CommentDetailDTO>> getCommentsByPhotoId(@PathVariable Long photoId) {
        List<CommentDetailDTO> comments = commentService.getCommentsByPhotoId(photoId);
        return ResponseEntity.ok(comments);
    }
}
