package com.photoAI.business.repository.keys;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Embeddable;

@Embeddable
public class FollowId implements Serializable {

    private Long followerId;
    private Long followingId;

    public FollowId() {}

    public FollowId(Long followerId, Long followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }

    // Getters and setters
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

    // hashCode and equals methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FollowId)) return false;
        FollowId followsId = (FollowId) o;
        return Objects.equals(getFollowerId(), followsId.getFollowerId()) &&
                Objects.equals(getFollowingId(), followsId.getFollowingId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFollowerId(), getFollowingId());
    }
}
