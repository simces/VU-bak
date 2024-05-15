package com.photo.model.likes;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LikeDetailDTO {

    private int likeCount;
    private List<String> usernames;
}
