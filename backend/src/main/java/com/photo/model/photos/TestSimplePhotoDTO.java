package com.photo.model.photos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestSimplePhotoDTO {

    private Long id;
    private String imageUrl;
}