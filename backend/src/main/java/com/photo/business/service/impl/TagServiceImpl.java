package com.photo.business.service.impl;

import com.photo.business.repository.PhotoTagRepository;
import com.photo.business.repository.TagRepository;
import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.repository.model.PhotoTagDAO;
import com.photo.business.repository.model.TagDAO;
import com.photo.business.service.TagService;
import com.photo.model.TagDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final PhotoTagRepository photoTagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, PhotoTagRepository photoTagRepository) {
        this.tagRepository = tagRepository;
        this.photoTagRepository = photoTagRepository;
    }

    @Override
    public TagDTO getTagByPhotoId(Long photoId) {
        Optional<PhotoTagDAO> photoTag = photoTagRepository.findByPhotoId(photoId);
        if (!photoTag.isPresent()) {
            throw new EntityNotFoundException("Tag not found for photoId: " + photoId);
        }
        Long tagId = photoTag.get().getTagId();

        TagDAO tagDAO = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + tagId));
        return new TagDTO(tagDAO.getId(), tagDAO.getName());
    }
}