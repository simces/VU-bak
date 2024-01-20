package com.photoAI.business.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class CommentDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "photo_id")
    private Long photoId;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "commented_at")
    private Timestamp commentedAt;
}
