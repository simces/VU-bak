package com.photo.model.photos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PhotoRankDTO {

    private Long id;
    private String imageUrl;
    private Long likeCount;
    private Long commentCount;

    public PhotoRankDTO(Long id, String imageUrl, Long likeCount, Long commentCount) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
