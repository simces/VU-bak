package com.photo.business.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "device_catalog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCatalogDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @Column(name = "model", length = 100, nullable = false)
    private String model;
}