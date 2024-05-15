package com.photo.business.service;

import com.photo.model.devices.UserDeviceDTO;

import java.util.List;

public interface UserDeviceService {

    UserDeviceDTO addUserDevice(Long userId, UserDeviceDTO userDeviceDTO);

    void deleteUserDevice(Long userId, Long deviceId);

    List<UserDeviceDTO> getUserDevices(Long userId);

    UserDeviceDTO updateUserDevice(Long userId, Long deviceId, UserDeviceDTO userDeviceDTO);
}
