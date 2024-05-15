package com.photo.controller;

import com.photo.business.service.FollowService;
import com.photo.model.follows.FollowRequestDTO;
import com.photo.model.follows.FollowResponseDTO;
import com.photo.model.follows.FollowStatusDTO;
import com.photo.model.follows.FollowerFollowingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/follow")
    public ResponseEntity<FollowResponseDTO> followUser(@RequestBody FollowRequestDTO followRequestDTO) {
        FollowResponseDTO followResponseDTO = followService.followUser(followRequestDTO);
        return ResponseEntity.ok(followResponseDTO);
    }

    @DeleteMapping("/unfollow/{followId}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long followId) {
        followService.unfollowUser(followId);
        return ResponseEntity.ok("Unfollowed successfully.");
    }

    @GetMapping("/isFollowing/{followingId}")
    public ResponseEntity<FollowStatusDTO> checkFollowing(@PathVariable Long followingId) {
        FollowStatusDTO followStatus = followService.checkFollowing(followingId);
        return ResponseEntity.ok(followStatus);
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<FollowerFollowingDTO> getFollowers(@PathVariable Long userId) {
        FollowerFollowingDTO followers = followService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<FollowerFollowingDTO> getFollowing(@PathVariable Long userId) {
        FollowerFollowingDTO following = followService.getFollowing(userId);
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