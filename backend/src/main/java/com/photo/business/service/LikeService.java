package com.photo.business.service;

import com.photo.model.likes.LikeDTO;
import com.photo.model.likes.LikeDetailDTO;
import com.photo.model.likes.LikeStatusDTO;

public interface LikeService {

    LikeDTO likePhoto(Long photoId);
    void unlikePhoto(Long likeId);
    LikeStatusDTO checkLikeStatus(Long photoId);
    LikeDetailDTO getLikeDetails(Long photoId);

}

