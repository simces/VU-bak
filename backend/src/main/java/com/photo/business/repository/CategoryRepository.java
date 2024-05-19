package com.photo.business.repository;

import com.photo.business.repository.model.CategoryDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryDAO, Long> {

    @Query("SELECT c FROM CategoryDAO c WHERE LOWER(c.name) = LOWER(:name) AND (:parent IS NULL OR c.parent = :parent)")
    Optional<CategoryDAO> findByNameAndParent(@Param("name") String name, @Param("parent") CategoryDAO parent);

    Optional<CategoryDAO> findByName(String name);

    @Query("SELECT c FROM CategoryDAO c WHERE c.parent IS NULL")
    List<CategoryDAO> findTopLevelCategories();

    @Query("SELECT c FROM CategoryDAO c WHERE c.parent = :parent")
    List<CategoryDAO> findSubcategories(@Param("parent") CategoryDAO parent);
}
