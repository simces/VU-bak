package com.photo.business.repository.model;

import jakarta.persistence.*;

@Entity
@Table(name = "follows", uniqueConstraints = @UniqueConstraint(columnNames = {"followerId", "followingId"}))
public class FollowDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long followerId;
    private Long followingId;

    // Constructors
    public FollowDAO() {}

    public FollowDAO(Long followerId, Long followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getFollowingId() {
        return followingId;
    }

    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }
}

