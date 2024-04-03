package com.photo.controller;

import com.photo.business.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/follow/{followingId}")
    public ResponseEntity<?> followUser(@PathVariable Long followingId) {
        followService.followUser(followingId);
        return ResponseEntity.ok("User followed successfully.");
    }

    @DeleteMapping("/unfollow/{followId}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long followId) {
        followService.unfollowUser(followId);
        return ResponseEntity.ok("Unfollowed successfully.");
    }

    @GetMapping("/isFollowing/{followingId}")
    public ResponseEntity<?> checkFollowing(@PathVariable Long followingId) {
        Map<String, Object> followStatus = followService.checkFollowing(followingId);
        return ResponseEntity.ok(followStatus);
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<Long>> getFollowers(@PathVariable Long userId) {
        List<Long> followers = followService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<List<Long>> getFollowing(@PathVariable Long userId) {
        List<Long> following = followService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/followers/count/{userId}")
    public ResponseEntity<Long> getFollowersCount(@PathVariable Long userId) {
        long count = followService.getFollowersCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/following/count/{userId}")
    public ResponseEntity<Long> getFollowingsCount(@PathVariable Long userId) {
        long count = followService.getFollowingsCount(userId);
        return ResponseEntity.ok(count);
    }
}

