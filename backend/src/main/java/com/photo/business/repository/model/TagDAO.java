package com.photo.business.repository.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tag")
public class TagDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ElementCollection
    @CollectionTable(name = "tag_hierarchy", joinColumns = @JoinColumn(name = "tag_id"))
    @Column(name = "path_element")
    private List<String> hierarchicalPath = new ArrayList<>();

    public TagDAO(String name, List<String> hierarchicalPath) {
        this.name = name;
        this.hierarchicalPath = hierarchicalPath;
    }
}