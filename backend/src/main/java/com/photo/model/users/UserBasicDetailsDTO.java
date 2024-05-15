package com.photo.model.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBasicDetailsDTO {

    private Long id;
    private String username;
    private String email;
    private String profilePictureUrl;
}

