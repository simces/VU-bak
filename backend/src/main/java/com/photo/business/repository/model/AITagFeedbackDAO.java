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
@Table(name = "aitagfeedback")
public class AITagFeedbackDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "photo_tag_id", nullable = false)
    private Long photoTagId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "feedback")
    private Boolean feedback;  // BIT type is typically mapped to Boolean in Java

    @Column(name = "created_at")
    private Timestamp createdAt;
}
