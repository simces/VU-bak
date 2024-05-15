package com.photo.business.service;

import com.photo.model.photos.FullPhotoDTO;
import com.photo.model.photos.PhotoDTO;
import com.photo.model.photos.PhotoResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {

    FullPhotoDTO getFullPhotoById(Long id);
    PhotoResponseDTO getPhotoResponseById(Long id);
    List<PhotoDTO> getPhotosByUserId(Long userId);
    void uploadPhotoFile(PhotoDTO photoDTO, MultipartFile file) throws IOException;

    void tagPhoto(Long photoId);
}
