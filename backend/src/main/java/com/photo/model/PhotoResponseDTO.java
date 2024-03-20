package com.photo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoResponseDTO {

    private PhotoDTO photoDTO;
    private UserProfileDTO userProfileDTO;
    private TagDTO tagDTO;
}

