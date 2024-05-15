package com.photo.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.photo.business.repository.model.DeviceCatalogDAO;

public interface DeviceCatalogRepository extends JpaRepository<DeviceCatalogDAO, Long> {
}
