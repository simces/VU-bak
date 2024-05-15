package com.photo.business.service;

import com.photo.business.repository.model.PhotoDAO;
import com.photo.model.TagDTO;

import java.math.BigDecimal;

public interface TagService {

    TagDTO getTagByPhotoId(Long photoId);
}
