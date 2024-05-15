package com.photo.model.photos;

import com.photo.model.tags.TagDTO;
import com.photo.model.users.UserBasicDetailsDTO;
import com.photo.model.users.UserProfileDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoResponseDTO {

    private FullPhotoDTO photo;
    private UserBasicDetailsDTO user;
}


