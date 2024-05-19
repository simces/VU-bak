package com.photo.model.photos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewPhotoDTO {

    private Long id;
    private String imageUrl;
    private Timestamp uploadedAt;
}
