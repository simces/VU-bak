package com.photo.business.repository.model;

import com.photo.business.repository.keys.PhotoTagId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "photo_tag")
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

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "tag_id", insertable = false, updatable = false)
    private TagDAO tag;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @MapsId("photoId")
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    private PhotoDAO photo;
}



