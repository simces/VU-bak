package com.photo.business.mappers;

import com.photo.business.repository.model.LikeDAO;
import com.photo.model.likes.LikeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LikeMapper {

    @Mapping(source = "photo.id", target = "photoId")
    @Mapping(source = "user.id", target = "userId")
    LikeDTO toDTO(LikeDAO like);

    @Mapping(source = "photoId", target = "photo.id")
    @Mapping(source = "userId", target = "user.id")
    LikeDAO toEntity(LikeDTO likeDTO);
}



