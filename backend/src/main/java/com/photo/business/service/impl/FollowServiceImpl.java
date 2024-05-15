package com.photo.business.service.impl;

import com.photo.business.mappers.FollowMapper;
import com.photo.business.repository.FollowRepository;
import com.photo.business.repository.UserRepository;
import com.photo.business.repository.model.FollowDAO;
import com.photo.business.repository.model.UserDAO;
import com.photo.business.service.FollowService;
import com.photo.model.FollowRequestDTO;
import com.photo.model.FollowResponseDTO;
import com.photo.model.FollowStatusDTO;
import com.photo.model.FollowerFollowingDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final FollowMapper followMapper;

    @Autowired
    public FollowServiceImpl(FollowRepository followRepository, UserRepository userRepository, FollowMapper followMapper) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.followMapper = followMapper;
    }

    @Override
    @Transactional
    public FollowResponseDTO followUser(FollowRequestDTO followRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserDAO follower = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Long followerId = follower.getId();
        Long followingId = followRequestDTO.getFollowingId();

        if (!followerId.equals(followingId) && !followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            FollowDAO follow = new FollowDAO(followerId, followingId);
            FollowDAO savedFollow = followRepository.save(follow);
            return followMapper.toResponseDTO(savedFollow);
        }
        throw new IllegalArgumentException("Invalid follow request");
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
    public FollowStatusDTO checkFollowing(Long followingId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDAO follower = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        Long followerId = follower.getId();

        Optional<FollowDAO> follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        if (follow.isPresent()) {
            return new FollowStatusDTO(true, follow.get().getId());
        } else {
            return new FollowStatusDTO(false, null);
        }
    }

    @Override
    public FollowerFollowingDTO getFollowers(Long userId) {
        List<Long> followerIds = followRepository.findFollowersOfUser(userId)
                .stream()
                .map(FollowDAO::getFollowerId)
                .collect(Collectors.toList());
        return new FollowerFollowingDTO(followerIds);
    }

    @Override
    public FollowerFollowingDTO getFollowing(Long userId) {
        List<Long> followingIds = followRepository.findFollowingsOfUser(userId)
                .stream()
                .map(FollowDAO::getFollowingId)
                .collect(Collectors.toList());
        return new FollowerFollowingDTO(followingIds);
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
