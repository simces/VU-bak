package com.photo.business.mappers;

import com.photo.business.repository.model.PhotoDAO;
import com.photo.model.photos.FullPhotoDTO;
import com.photo.model.photos.PhotoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhotoMapper {

    @Mapping(source = "device.id", target = "deviceId")
    PhotoDTO photoDAOToPhotoDTO(PhotoDAO photoDAO);

    @Mapping(source = "deviceId", target = "device.id")
    PhotoDAO photoDTOToPhotoDAO(PhotoDTO photoDTO);

    @Mapping(source = "device.id", target = "device.id")
    @Mapping(source = "device.type", target = "device.type")
    @Mapping(source = "device.model", target = "device.model")
    FullPhotoDTO photoDAOToFullPhotoDTO(PhotoDAO photoDAO);

    
}
