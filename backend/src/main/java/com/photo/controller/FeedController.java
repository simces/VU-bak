package com.photo.controller;

import com.photo.business.service.FeedService;
import com.photo.model.photos.PhotoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping
    public ResponseEntity<Page<PhotoResponseDTO>> getFeed(@PageableDefault(size = 10) Pageable pageable) {
        Page<PhotoResponseDTO> feed = feedService.getFeed(pageable);
        return ResponseEntity.ok(feed);
    }
}