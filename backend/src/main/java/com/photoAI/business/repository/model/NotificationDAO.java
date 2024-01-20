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
@Table(name = "notifications")
public class NotificationDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "type_id", nullable = false)
    private Long typeId;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "read_status")
    private Boolean readStatus;  // BIT type mapped to boolean in Java

    @Column(name = "created_at")
    private Timestamp createdAt;

}
