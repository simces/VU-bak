package com.photoAI.business.repository.model;

import com.photoAI.business.repository.keys.PhotoTagId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "phototag")
@IdClass(PhotoTagId.class)
public class PhotoTagDAO {

    @Id
    @Column(name = "photo_id")
    private Long photoId;

    @Id
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "confidence")
    private BigDecimal confidence;

}

