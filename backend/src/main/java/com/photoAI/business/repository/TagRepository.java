package com.photoAI.business.repository;

import com.photoAI.business.repository.model.TagDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagDAO, Long> {
}
