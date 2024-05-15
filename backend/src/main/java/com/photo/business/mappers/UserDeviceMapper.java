package com.photo.business.mappers;

import com.photo.business.repository.model.UserDeviceDAO;
import com.photo.model.UserDeviceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDeviceMapper {

    @Mapping(source = "user.id", target = "userId")
    UserDeviceDTO toDto(UserDeviceDAO userDeviceDAO);

    @Mapping(source = "userId", target = "user.id")
    UserDeviceDAO toDao(UserDeviceDTO userDeviceDTO);
}
