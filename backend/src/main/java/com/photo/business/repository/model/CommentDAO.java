package com.photo.business.repository.model;

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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDAO user;

    @ManyToOne
    @JoinColumn(name = "photo_id", nullable = false)
    private PhotoDAO photo;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "commented_at")
    private Timestamp commentedAt;
}
