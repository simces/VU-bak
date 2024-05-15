package com.photo.business.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_devices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDeviceDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDAO user;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "model", length = 100)
    private String model;
}
