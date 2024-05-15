package com.photo.business.service.impl;

import com.photo.business.handlers.exceptions.UserNotFoundException;
import com.photo.business.mappers.UserDeviceMapper;
import com.photo.business.repository.UserDeviceRepository;
import com.photo.business.repository.UserRepository;
import com.photo.business.repository.model.UserDAO;
import com.photo.business.repository.model.UserDeviceDAO;
import com.photo.business.service.UserDeviceService;
import com.photo.model.devices.UserDeviceDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDeviceServiceImpl implements UserDeviceService {

    private final UserDeviceRepository userDeviceRepository;
    private final UserRepository userRepository;
    private final UserDeviceMapper userDeviceMapper;

    public UserDeviceServiceImpl(UserDeviceRepository userDeviceRepository, UserRepository userRepository, UserDeviceMapper userDeviceMapper) {
        this.userDeviceRepository = userDeviceRepository;
        this.userRepository = userRepository;
        this.userDeviceMapper = userDeviceMapper;
    }

    @Transactional
    @Override
    public UserDeviceDTO addUserDevice(Long userId, UserDeviceDTO userDeviceDTO) {
        UserDAO user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        UserDeviceDAO newUserDevice = userDeviceMapper.toDao(userDeviceDTO);
        newUserDevice.setUser(user);

        newUserDevice = userDeviceRepository.save(newUserDevice);
        return userDeviceMapper.toDto(newUserDevice);
    }

    @Transactional
    @Override
    public void deleteUserDevice(Long userId, Long deviceId) {
        UserDeviceDAO device = userDeviceRepository.findByIdAndUserId(deviceId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found with id: " + deviceId + " for user: " + userId));
        userDeviceRepository.delete(device);
    }

    @Override
    public List<UserDeviceDTO> getUserDevices(Long userId) {
        return userDeviceRepository.findByUserId(userId).stream()
                .map(userDeviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDeviceDTO updateUserDevice(Long userId, Long deviceId, UserDeviceDTO userDeviceDTO) {
        UserDeviceDAO userDevice = userDeviceRepository.findByIdAndUserId(deviceId, userId)
                .orElseThrow(() -> new IllegalArgumentException("No device found with id: " + deviceId + " for user: " + userId));

        userDevice.setType(userDeviceDTO.getType());
        userDevice.setModel(userDeviceDTO.getModel());
        userDeviceRepository.save(userDevice);

        return userDeviceMapper.toDto(userDevice);
    }
}
