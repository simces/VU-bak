package com.photo.model.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDetailDTO {

    private Long id;
    private Long userId;
    private String username;
    private Long photoId;
    private String comment;
    private Timestamp commentedAt;
}
