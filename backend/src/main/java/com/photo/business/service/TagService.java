package com.photo.business.service;

import com.photo.model.tags.TagDTO;

import java.util.List;

public interface TagService {

    List<TagDTO> getTagsByPhotoId(Long photoId);
}
