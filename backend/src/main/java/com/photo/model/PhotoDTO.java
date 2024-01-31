package com.photo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDTO {

    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String imageUrl;
    private Timestamp uploadedAt;

}

