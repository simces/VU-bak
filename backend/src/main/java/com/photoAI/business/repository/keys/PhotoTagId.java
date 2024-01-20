package com.photoAI.business.repository.keys;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Embeddable;

@Embeddable
public class PhotoTagId implements Serializable {

    private Long photoId;
    private Long tagId;

    public PhotoTagId() {}

    public PhotoTagId(Long photoId, Long tagId) {
        this.photoId = photoId;
        this.tagId = tagId;
    }

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    // hashCode and equals methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoTagId that = (PhotoTagId) o;
        return Objects.equals(photoId, that.photoId) && Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoId, tagId);
    }
}

