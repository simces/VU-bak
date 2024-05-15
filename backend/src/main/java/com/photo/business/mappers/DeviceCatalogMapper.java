package com.photo.business.mappers;

import com.photo.business.repository.model.DeviceCatalogDAO;
import com.photo.model.devices.DeviceCatalogDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeviceCatalogMapper {

    DeviceCatalogDTO toDto(DeviceCatalogDAO deviceCatalogDAO);

    DeviceCatalogDAO toDao(DeviceCatalogDTO deviceCatalogDTO);
}
