package com.photo.business.mappers;

import com.photo.business.repository.model.PhotoDAO;
import com.photo.model.photos.FullPhotoDTO;
import com.photo.model.photos.PhotoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhotoMapper {

    PhotoDTO photoDAOToPhotoDTO(PhotoDAO photoDAO);

    PhotoDAO photoDTOToPhotoDAO(PhotoDTO photoDTO);

    FullPhotoDTO photoDAOToFullPhotoDTO(PhotoDAO photoDAO);
}
