package com.photo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowDTO {

    private Long followerId;
    private Long followingId;
}
