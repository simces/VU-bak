package com.photo.model;

public class LikeStatusDTO {
    private boolean isLiked;
    private Long likeId;

    public LikeStatusDTO(boolean isLiked, Long likeId) {
        this.isLiked = isLiked;
        this.likeId = likeId;
    }

    // Getters and Setters
    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public Long getLikeId() {
        return likeId;
    }

    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }
}

