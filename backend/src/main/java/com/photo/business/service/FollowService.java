package com.photo.business.service;

import com.photo.model.FollowRequestDTO;
import com.photo.model.FollowResponseDTO;
import com.photo.model.FollowStatusDTO;
import com.photo.model.FollowerFollowingDTO;

import java.util.List;
import java.util.Map;

public interface FollowService {

    FollowResponseDTO followUser(FollowRequestDTO followRequestDTO);
    void unfollowUser(Long followId);

    FollowStatusDTO checkFollowing(Long followingId);

    FollowerFollowingDTO getFollowers(Long userId);

    FollowerFollowingDTO getFollowing(Long userId);

    long getFollowersCount(Long userId);

    long getFollowingsCount(Long userId);
}
