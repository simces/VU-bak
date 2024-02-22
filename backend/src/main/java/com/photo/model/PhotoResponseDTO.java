package com.photo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Timestamp uploadedAt;

    private String username;
    private String profilePictureUrl;


    public PhotoResponseDTO(PhotoDTO photoDTO, UserProfileDTO userProfileDTO) {
        this.id = photoDTO.getId();
        this.title = photoDTO.getTitle();
        this.description = photoDTO.getDescription();
        this.imageUrl = photoDTO.getImageUrl();
        this.uploadedAt = photoDTO.getUploadedAt();
        this.username = userProfileDTO.getUsername();
        this.profilePictureUrl = userProfileDTO.getProfilePictureUrl();
    }
}

