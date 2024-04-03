package com.photo.business.service.impl;

import com.photo.business.repository.FollowRepository;
import com.photo.business.repository.UserRepository;
import com.photo.business.repository.model.FollowDAO;
import com.photo.business.repository.model.UserDAO;
import com.photo.business.service.FollowService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Autowired
    public FollowServiceImpl(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
        public void followUser(Long followingId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserDAO follower = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Long followerId = follower.getId();
        if (!followerId.equals(followingId) && !followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            FollowDAO follow = new FollowDAO(followerId, followingId);
            followRepository.save(follow);
        }
    }



    @Override
    @Transactional
    public void unfollowUser(Long followId) {
        FollowDAO follow = followRepository.findById(followId)
                .orElseThrow(() -> new EntityNotFoundException("Follow not found with id: " + followId));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        UserDAO currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + currentUsername));

        if (!follow.getFollowerId().equals(currentUser.getId())) {
            throw new AccessDeniedException("User not authorized to perform this operation");
        }

        followRepository.delete(follow);
    }

    @Override
    public Map<String, Object> checkFollowing(Long followingId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDAO follower = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        Long followerId = follower.getId();

        Optional<FollowDAO> follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        if (follow.isPresent()) {
            return Map.of("isFollowing", true, "followId", follow.get().getId());
        } else {
            return Map.of("isFollowing", false);
        }
    }


    @Override
    public List<Long> getFollowers(Long userId) {
        return followRepository.findFollowersOfUser(userId)
                .stream()
                .map(FollowDAO::getFollowerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getFollowing(Long userId) {
        return followRepository.findFollowingsOfUser(userId)
                .stream()
                .map(FollowDAO::getFollowingId)
                .collect(Collectors.toList());
    }

    @Override
    public long getFollowersCount(Long userId) {
        return followRepository.countFollowersOfUser(userId);
    }

    @Override
    public long getFollowingsCount(Long userId) {
        return followRepository.countFollowingsOfUser(userId);
    }
}