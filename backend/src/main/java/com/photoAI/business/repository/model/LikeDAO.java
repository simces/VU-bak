package com.photoAI.business.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "likes")
public class LikeDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "photo_id", nullable = false)
    private Long photoId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // maybe map relationships later? not sure if needed
    // @ManyToOne
    // @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    // private PhotoDAO photo;

    // @ManyToOne
    // @JoinColumn(name = "user_id", insertable = false, updatable = false)
    // private UserDAO user;
}

