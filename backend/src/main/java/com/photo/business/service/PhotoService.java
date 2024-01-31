package com.photo.business.service;

import com.photo.model.PhotoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhotoService {

    PhotoDTO uploadPhoto(PhotoDTO photoDTO);

    PhotoDTO getPhotoById(Long id);

    void uploadPhotoFile(PhotoDTO photoDTO, MultipartFile file) throws IOException;
}
