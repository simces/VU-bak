package com.photo.business.mappers;

import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.repository.model.PhotoTagDAO;
import com.photo.model.photos.EssentialPhotoDTO;
import com.photo.model.photos.FullPhotoDTO;
import com.photo.model.photos.PhotoDTO;
import com.photo.model.tags.TagDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.stream.Collectors;

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