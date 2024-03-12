package com.photo.business.repository;

import com.photo.business.repository.model.TagDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<TagDAO, Long> {

    Optional<TagDAO> findByName(String name);

}
