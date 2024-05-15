package com.photo.model.likes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeStatusDTO {

    private boolean isLiked;
    private Long likeId;
}

