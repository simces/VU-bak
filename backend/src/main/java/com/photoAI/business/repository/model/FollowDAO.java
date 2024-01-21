package com.photoAI.business.repository.model;
import com.photoAI.business.repository.keys.FollowId;
import jakarta.persistence.*;

@Entity
public class FollowDAO {

    @EmbeddedId
    private FollowId id;

    // Constructors, getters, setters
    public FollowDAO() {}

    public FollowDAO(FollowId id) {
        this.id = id;
    }

    public FollowId getId() {
        return id;
    }

    public void setId(FollowId id) {
        this.id = id;
    }
}
