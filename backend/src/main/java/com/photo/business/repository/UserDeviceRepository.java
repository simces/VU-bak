package com.photo.business.repository;

import com.photo.business.repository.model.UserDeviceDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDeviceDAO, Long> {

    List<UserDeviceDAO> findByUserId(Long userId);

    Optional<UserDeviceDAO> findByIdAndUserId(Long id, Long userId);
}
