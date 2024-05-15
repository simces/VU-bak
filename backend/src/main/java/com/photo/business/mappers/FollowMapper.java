package com.photo.business.mappers;

import com.photo.business.repository.model.FollowDAO;
import com.photo.model.follows.FollowResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FollowMapper {

    FollowResponseDTO toResponseDTO(FollowDAO followDAO);
}
