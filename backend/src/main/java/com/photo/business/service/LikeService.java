package com.photo.business.service;

import com.photo.business.repository.model.LikeDAO;
import com.photo.model.LikeStatusDTO;

public interface LikeService {

    LikeDAO likePhoto(Long photoId);

    void unlikePhoto(Long likeId);

    LikeStatusDTO checkLikeStatus(Long photoId);
}

