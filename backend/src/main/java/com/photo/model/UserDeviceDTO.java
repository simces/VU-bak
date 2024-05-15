package com.photo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDeviceDTO {

    private Long id;
    private Long userId;
    private String type;
    private String model;
}