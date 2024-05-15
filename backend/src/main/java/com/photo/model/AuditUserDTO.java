package com.photo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditUserDTO {

    private Long id;
    private String username;
    private String email;
    private String profilePictureUrl;
    private String bio;
    private String role;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
