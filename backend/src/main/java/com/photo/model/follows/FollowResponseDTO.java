package com.photo.model.follows;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowResponseDTO {

    private Long id;
    private Long followerId;
    private Long followingId;
}

