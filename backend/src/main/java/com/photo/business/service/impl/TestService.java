package com.photo.business.service.impl;

import com.photo.business.mappers.PhotoMapper;
import com.photo.business.repository.PhotoRepository;
import com.photo.business.repository.model.PhotoDAO;
import com.photo.model.photos.TestFullPhotoDTO;
import com.photo.model.photos.TestSimplePhotoDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


// main point of this class is to compare the performance
// between fetching photo using the full DAO, DTO, and a minimized DTO
// this shows the impact that having optimized data actually matters

// also, the latter methods demonstrate the importance of pagination
// since there's no point to fetch a million photos, when you only need 10


@Service
public class TestService {

    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;

    public TestService(PhotoRepository photoRepository, PhotoMapper photoMapper) {
        this.photoRepository = photoRepository;
        this.photoMapper = photoMapper;
    }

    public List<PhotoDAO> getAllPhotosAsDAO() {
        return photoRepository.findAll();
    }

    public List<TestFullPhotoDTO> getAllPhotosAsTestPhotoDTO() {
        return photoRepository.findAll().stream()
                .map(photoMapper::photoDAOToTestFullPhotoDTO)
                .collect(Collectors.toList());
    }

    public List<TestSimplePhotoDTO> getAllPhotosAsSimpleDTO() {
        return photoRepository.findAll().stream()
                .map(photoMapper::photoDAOToTestSimplePhotoDTO)
                .collect(Collectors.toList());
    }
}
