package com.photo.business.service.impl;

import com.photo.business.repository.PhotoTagRepository;
import com.photo.business.repository.TagRepository;
import com.photo.business.repository.model.PhotoTagDAO;
import com.photo.business.repository.model.TagDAO;
import com.photo.business.service.TagService;
import com.photo.model.tags.TagDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<TagDTO> getTagsByPhotoId(Long photoId) {
        List<PhotoTagDAO> photoTags = photoTagRepository.findByPhotoId(photoId);
        if (photoTags.isEmpty()) {
            throw new EntityNotFoundException("Tags not found for photoId: " + photoId);
        }
        return photoTags.stream()
                .map(photoTag -> new TagDTO(photoTag.getTag().getId(), photoTag.getTag().getName()))
                .collect(Collectors.toList());
    }
}