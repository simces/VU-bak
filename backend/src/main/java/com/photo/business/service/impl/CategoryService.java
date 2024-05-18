package com.photo.business.service.impl;

import com.photo.business.repository.CategoryRepository;
import com.photo.business.repository.PhotoTagRepository;
import com.photo.business.repository.model.CategoryDAO;
import com.photo.business.repository.model.PhotoTagDAO;
import com.photo.model.CategoryWithCountDTO;
import com.photo.model.photos.SimplePhotoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final PhotoTagRepository photoTagRepository;

    public CategoryService(CategoryRepository categoryRepository, PhotoTagRepository photoTagRepository) {
        this.categoryRepository = categoryRepository;
        this.photoTagRepository = photoTagRepository;
    }

    public List<CategoryWithCountDTO> getTopLevelCategoriesWithCounts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryDAO> topLevelCategories = categoryRepository.findByParentIsNull(pageable);
        return topLevelCategories.stream()
                .map(this::mapToCategoryWithCountDTO)
                .sorted(Comparator.comparing(CategoryWithCountDTO::getPhotoCount).reversed())
                .collect(Collectors.toList());
    }


    public Object getPhotosOrSubcategories(String categoryName, int page, int size) {
        CategoryDAO category = findCategoryByName(categoryName);
        List<CategoryDAO> subcategories = categoryRepository.findByParent(category);

        if (subcategories.isEmpty()) {
            return getAllPhotosByCategory(categoryName, page, size);
        } else {
            return subcategories.stream()
                    .map(this::mapToCategoryWithCountDTO)
                    .sorted(Comparator.comparing(CategoryWithCountDTO::getPhotoCount).reversed())
                    .collect(Collectors.toList());
        }
    }

    public List<SimplePhotoDTO> getAllPhotosByCategory(String categoryName, int page, int size) {
        CategoryDAO category = findCategoryByName(categoryName);
        List<Long> categoryIds = getCategoryIdsIncludingSubcategories(category);
        Pageable pageable = PageRequest.of(page, size);
        List<PhotoTagDAO> photoTags = photoTagRepository.findByCategoryIdIn(categoryIds, pageable);
        return photoTags.stream()
                .map(photoTag -> new SimplePhotoDTO(photoTag.getPhoto()))
                .collect(Collectors.toList());
    }

    private CategoryDAO findCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    private CategoryWithCountDTO mapToCategoryWithCountDTO(CategoryDAO category) {
        int photoCount = countPhotosInCategory(category);
        return new CategoryWithCountDTO(category.getName(), photoCount);
    }

    private int countPhotosInCategory(CategoryDAO category) {
        int count = photoTagRepository.countByCategory(category);
        for (CategoryDAO subcategory : category.getChildren()) {
            count += countPhotosInCategory(subcategory);
        }
        return count;
    }

    private List<Long> getCategoryIdsIncludingSubcategories(CategoryDAO category) {
        List<Long> categoryIds = new ArrayList<>();
        categoryIds.add(category.getId());
        for (CategoryDAO subcategory : category.getChildren()) {
            categoryIds.addAll(getCategoryIdsIncludingSubcategories(subcategory));
        }
        return categoryIds;
    }
}