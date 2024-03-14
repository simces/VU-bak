package com.photo.controller;

import com.photo.business.handlers.exceptions.UserNotFoundException;
import com.photo.business.repository.model.CommentDAO;
import com.photo.business.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<CommentDAO> addComment(@PathVariable Long photoId, @RequestBody CommentDAO comment) {
        comment.setPhotoId(photoId);
        CommentDAO savedComment = commentService.addCommentToPhoto(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }


    @GetMapping
    public ResponseEntity<List<CommentDAO>> getCommentsByPhotoId(@PathVariable Long photoId) {
        List<CommentDAO> comments = commentService.getCommentsByPhotoId(photoId);
        return ResponseEntity.ok(comments);
    }
}
