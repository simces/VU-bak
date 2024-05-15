package com.photo.controller;

import com.photo.business.handlers.exceptions.UserNotFoundException;
import com.photo.business.service.UserDeviceService;
import com.photo.model.devices.UserDeviceDTO;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/devices")
public class UserDeviceController {

    private final UserDeviceService userDeviceService;

    public UserDeviceController(UserDeviceService userDeviceService) {
        this.userDeviceService = userDeviceService;
    }

    @PostMapping
    public ResponseEntity<UserDeviceDTO> addDeviceToUser(@PathVariable Long userId, @RequestBody UserDeviceDTO userDeviceDTO) {
        try {
            UserDeviceDTO savedDevice = userDeviceService.addUserDevice(userId, userDeviceDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDevice);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long userId, @PathVariable Long deviceId) {
        try {
            userDeviceService.deleteUserDevice(userId, deviceId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDeviceDTO>> getUserDevices(@PathVariable Long userId) {
        List<UserDeviceDTO> devices = userDeviceService.getUserDevices(userId);
        return ResponseEntity.ok(devices);
    }

    @PutMapping("/{deviceId}")
    public ResponseEntity<UserDeviceDTO> updateDevice(@PathVariable Long userId, @PathVariable Long deviceId, @RequestBody UserDeviceDTO userDeviceDTO) {
        try {
            UserDeviceDTO updatedDevice = userDeviceService.updateUserDevice(userId, deviceId, userDeviceDTO);
            return ResponseEntity.ok(updatedDevice);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
