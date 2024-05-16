package com.photo.model.photos;

import com.photo.model.devices.DeviceDTO;
import com.photo.model.tags.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullPhotoDTO {

    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String imageUrl;
    private Timestamp uploadedAt;
    private Double latitude;
    private Double longitude;
    private String location;
    private DeviceDTO device;
    private List<TagDTO> tags;

}

