package com.photo.business.mappers;

import com.photo.business.repository.model.PhotoDAO;
import com.photo.model.photos.FullPhotoDTO;
import com.photo.model.photos.PhotoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PhotoMapper {

    @Mapping(source = "device.id", target = "deviceId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    PhotoDTO photoDAOToPhotoDTO(PhotoDAO photoDAO);

    @Mapping(source = "deviceId", target = "device.id", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    PhotoDAO photoDTOToPhotoDAO(PhotoDTO photoDTO);

    @Mapping(source = "device.id", target = "device.id")
    @Mapping(source = "device.type", target = "device.type")
    @Mapping(source = "device.model", target = "device.model")
    FullPhotoDTO photoDAOToFullPhotoDTO(PhotoDAO photoDAO);
}

