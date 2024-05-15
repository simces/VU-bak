package com.photo.business.mappers;

import com.photo.business.repository.model.CommentDAO;
import com.photo.model.comments.CommentDTO;
import com.photo.model.comments.CommentDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "photo.id", target = "photoId")
    CommentDTO toDTO(CommentDAO comment);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "photo.id", target = "photoId")
    CommentDetailDTO toDetailDTO(CommentDAO comment);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "photo", ignore = true)
    CommentDAO toEntity(CommentDTO commentDTO);

}