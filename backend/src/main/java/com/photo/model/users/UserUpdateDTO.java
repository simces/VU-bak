package com.photo.model.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    private String username;
    private String email;
    private String profilePictureUrl;
    private String bio;
    private String role;
}
