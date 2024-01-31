package com.photo.business.mappers;

import com.photo.business.repository.model.PhotoDAO;
import com.photo.model.PhotoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PhotoMapper {

    PhotoMapper INSTANCE = Mappers.getMapper(PhotoMapper.class);

    PhotoDTO photoDAOToPhotoDTO(PhotoDAO photoDAO);

    PhotoDAO photoDTOToPhotoDAO(PhotoDTO photoDTO);
}
