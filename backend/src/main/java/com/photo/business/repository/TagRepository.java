package com.photo.business.repository;

import com.photo.business.repository.model.TagDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagDAO, Long> {
}
