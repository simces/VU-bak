package com.photo.model.photos;

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
    private List<TagDTO> tags;
}

