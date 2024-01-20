package com.photoAI.business.repository.model;
import jakarta.persistence.*;

public class FollowDAO {

    @Id
    @Column(name = "follower_id")
    private Long followerId;

    @Id
    @Column(name = "following_id")
    private Long followingId;
}
