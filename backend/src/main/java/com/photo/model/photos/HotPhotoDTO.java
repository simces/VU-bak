package com.photo.model.photos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotPhotoDTO {

    private Long id;
    private String imageUrl;
    private Long likeCount;
}
