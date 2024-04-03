package com.photo.business.service;

import java.util.List;
import java.util.Map;

public interface FollowService {

    void followUser(Long followingId);

    void unfollowUser(Long followId);

    Map<String, Object> checkFollowing(Long followingId);

    List<Long> getFollowers(Long userId);

    List<Long> getFollowing(Long userId);

    long getFollowersCount(Long userId);

    long getFollowingsCount(Long userId);
}
