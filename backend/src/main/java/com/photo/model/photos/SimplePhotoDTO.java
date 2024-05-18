package com.photo.model.photos;

import com.photo.business.repository.model.PhotoDAO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimplePhotoDTO {
    private Long id;
    private String imageUrl;

    public SimplePhotoDTO(PhotoDAO photo) {
        this.id = photo.getId();
        this.imageUrl = photo.getImageUrl();
    }
}
