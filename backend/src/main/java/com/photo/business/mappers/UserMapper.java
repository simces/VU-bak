package com.photo.business.mappers;

import com.photo.business.repository.model.UserDAO;
import com.photo.model.UserCreationDTO;
import com.photo.model.UserProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profilePictureUrl", ignore = true)
    @Mapping(target = "bio", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserDAO userCreationDTOToUserDAO(UserCreationDTO userCreationDTO);

    @Mapping(target = "password", ignore = true)
    UserDAO userProfileDTOToUserDAO(UserProfileDTO userProfileDTO);

    UserProfileDTO userDAOToUserProfileDTO(UserDAO userDAO);
}

