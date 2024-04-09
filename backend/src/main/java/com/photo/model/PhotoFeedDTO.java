package com.photo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoFeedDTO {
    private Long id;
    private String username;
    private String imageUrl;
    private Set<String> tags;
    private String uploadedAt;
}