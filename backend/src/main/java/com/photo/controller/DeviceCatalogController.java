package com.photo.controller;

import com.photo.business.service.DeviceCatalogService;
import com.photo.model.devices.DeviceCatalogDTO;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices/catalog")
public class DeviceCatalogController {

    private final DeviceCatalogService deviceCatalogService;

    public DeviceCatalogController(DeviceCatalogService deviceCatalogService) {
        this.deviceCatalogService = deviceCatalogService;
    }

    @GetMapping
    public ResponseEntity<List<DeviceCatalogDTO>> getAllDevices() {
        List<DeviceCatalogDTO> devices = deviceCatalogService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeviceCatalogDTO> createDeviceCatalog(@RequestBody DeviceCatalogDTO deviceDTO) {
        DeviceCatalogDTO newDevice = deviceCatalogService.createDeviceCatalogEntry(deviceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDevice);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeviceCatalogDTO> updateDeviceCatalog(@PathVariable Long id, @RequestBody DeviceCatalogDTO deviceDTO) {
        DeviceCatalogDTO updatedDevice = deviceCatalogService.updateDeviceCatalogEntry(id, deviceDTO);
        return ResponseEntity.ok(updatedDevice);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDeviceCatalog(@PathVariable Long id) {
        deviceCatalogService.deleteDeviceCatalogEntry(id);
        return ResponseEntity.noContent().build();
    }
}
