package com.photo.business.service.impl;

import com.photo.business.mappers.DeviceCatalogMapper;
import com.photo.business.repository.DeviceCatalogRepository;
import com.photo.business.repository.model.DeviceCatalogDAO;
import com.photo.business.service.DeviceCatalogService;
import com.photo.model.DeviceCatalogDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceCatalogServiceImpl implements DeviceCatalogService {

    private final DeviceCatalogRepository deviceCatalogRepository;
    private final DeviceCatalogMapper deviceCatalogMapper;

    public DeviceCatalogServiceImpl(DeviceCatalogRepository deviceCatalogRepository, DeviceCatalogMapper deviceCatalogMapper) {
        this.deviceCatalogRepository = deviceCatalogRepository;
        this.deviceCatalogMapper = deviceCatalogMapper;
    }

    @Override
    public List<DeviceCatalogDTO> getAllDevices() {
        return deviceCatalogRepository.findAll().stream()
                .map(deviceCatalogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public DeviceCatalogDTO createDeviceCatalogEntry(DeviceCatalogDTO deviceDTO) {
        DeviceCatalogDAO newDevice = deviceCatalogMapper.toDao(deviceDTO);
        newDevice = deviceCatalogRepository.save(newDevice);
        return deviceCatalogMapper.toDto(newDevice);
    }

    @Transactional
    @Override
    public DeviceCatalogDTO updateDeviceCatalogEntry(Long id, DeviceCatalogDTO deviceDTO) {
        DeviceCatalogDAO device = deviceCatalogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Device not found with id: " + id));
        device.setType(deviceDTO.getType());
        device.setModel(deviceDTO.getModel());
        device = deviceCatalogRepository.save(device);
        return deviceCatalogMapper.toDto(device);
    }

    @Transactional
    @Override
    public void deleteDeviceCatalogEntry(Long id) {
        DeviceCatalogDAO device = deviceCatalogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Device not found with id: " + id));
        deviceCatalogRepository.delete(device);
    }
}