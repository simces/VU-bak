package com.photo.business.service.impl;

import com.photo.business.repository.PhotoTagRepository;
import com.photo.business.repository.TagRepository;
import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.repository.model.PhotoTagDAO;
import com.photo.business.repository.model.TagDAO;
import com.photo.business.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PhotoTagRepository photoTagRepository;

    @Override
    public void saveTag(String tagName, PhotoDAO photo, BigDecimal confidence) {
        TagDAO tag = tagRepository.findByName(tagName)
                .orElseGet(() -> tagRepository.save(new TagDAO(null, tagName)));

        PhotoTagDAO photoTag = new PhotoTagDAO();
        photoTag.setPhoto(photo);
        photoTag.setTag(tag);
        photoTag.setConfidence(confidence);

        photoTagRepository.save(photoTag);
    }
}