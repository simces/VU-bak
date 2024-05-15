package com.photo.business.mappers;

import com.photo.business.repository.model.UserDAO;
import com.photo.model.audits.AuditUserDTO;
import com.photo.model.users.UserBasicDetailsDTO;
import com.photo.model.users.UserCreationDTO;
import com.photo.model.users.UserDTO;
import com.photo.model.users.UserProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profilePictureUrl", ignore = true)
    @Mapping(target = "bio", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    UserDAO userCreationDTOToUserDAO(UserCreationDTO userCreationDTO);

    UserProfileDTO userDAOToUserProfileDTO(UserDAO userDAO);

    UserDTO userDAOToUserDTO(UserDAO userDAO);

    AuditUserDTO userDAOToAuditUserDTO(UserDAO userDAO);

    UserBasicDetailsDTO userDAOToUserBasicDetailsDTO(UserDAO userDAO);
}

