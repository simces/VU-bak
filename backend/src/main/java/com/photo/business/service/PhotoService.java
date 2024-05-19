package com.photo.business.service;

import com.photo.model.photos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {

    FullPhotoDTO getFullPhotoById(Long id);
    PhotoResponseDTO getPhotoResponseById(Long id);
    List<PhotoDTO> getPhotosByUserId(Long userId);
    void uploadPhotoFile(PhotoDTO photoDTO, MultipartFile file) throws IOException;
    void tagPhoto(Long photoId);
    List<HotPhotoDTO> getHotPhotos(int page, int size);
    Page<PhotoRankDTO> getTopRankedPhotos(Pageable pageable);
    Page<NewPhotoDTO> getNewPhotos(Pageable pageable);
}
