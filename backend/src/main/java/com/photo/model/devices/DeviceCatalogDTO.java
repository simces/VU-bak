package com.photo.model.devices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCatalogDTO {

    private Long id;
    private String type;
    private String model;
}

