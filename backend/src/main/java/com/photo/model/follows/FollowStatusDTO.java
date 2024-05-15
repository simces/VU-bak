package com.photo.model.follows;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowStatusDTO {

    private boolean isFollowing;
    private Long followId;
}
