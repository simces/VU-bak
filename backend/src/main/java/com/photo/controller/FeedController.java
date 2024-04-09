package com.photo.controller;

import com.photo.business.service.FeedService;
import com.photo.model.PhotoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private FeedService feedService;

    @GetMapping
    public ResponseEntity<Page<PhotoResponseDTO>> getFeed(@PageableDefault(size = 10) Pageable pageable) {
        Page<PhotoResponseDTO> feed = feedService.getFeed(pageable);
        return ResponseEntity.ok(feed);
    }
}