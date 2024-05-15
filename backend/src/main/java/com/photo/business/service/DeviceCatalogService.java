package com.photo.business.service;

import java.util.List;
import com.photo.model.DeviceCatalogDTO;

public interface DeviceCatalogService {

    List<DeviceCatalogDTO> getAllDevices();

    DeviceCatalogDTO createDeviceCatalogEntry(DeviceCatalogDTO deviceDTO);

    DeviceCatalogDTO updateDeviceCatalogEntry(Long id, DeviceCatalogDTO deviceDTO);

    void deleteDeviceCatalogEntry(Long id);
}
