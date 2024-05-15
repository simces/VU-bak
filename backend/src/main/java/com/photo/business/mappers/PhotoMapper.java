package com.photo.business.mappers;

import com.photo.business.repository.model.PhotoDAO;
import com.photo.model.FullPhotoDTO;
import com.photo.model.PhotoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhotoMapper {

    PhotoDTO photoDAOToPhotoDTO(PhotoDAO photoDAO);

    PhotoDAO photoDTOToPhotoDAO(PhotoDTO photoDTO);

    @Mapping(target = "tags", ignore = true) // Tags will be set manually
    FullPhotoDTO photoDAOToFullPhotoDTO(PhotoDAO photoDAO);

    @Mapping(target = "photoTags", ignore = true) // Tags will be set manually
    PhotoDAO fullPhotoDTOToPhotoDAO(FullPhotoDTO fullPhotoDTO);
}
