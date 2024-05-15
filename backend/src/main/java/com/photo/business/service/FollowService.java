package com.photo.business.service;

import com.photo.model.follows.FollowRequestDTO;
import com.photo.model.follows.FollowResponseDTO;
import com.photo.model.follows.FollowStatusDTO;
import com.photo.model.follows.FollowerFollowingDTO;

public interface FollowService {

    FollowResponseDTO followUser(FollowRequestDTO followRequestDTO);
    void unfollowUser(Long followId);

    FollowStatusDTO checkFollowing(Long followingId);

    FollowerFollowingDTO getFollowers(Long userId);

    FollowerFollowingDTO getFollowing(Long userId);

    long getFollowersCount(Long userId);

    long getFollowingsCount(Long userId);
}
