package com.photo.business.service;

import com.photo.model.photos.PhotoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface FeedService {

    Page<PhotoResponseDTO> getFeed(Pageable pageable);
}
