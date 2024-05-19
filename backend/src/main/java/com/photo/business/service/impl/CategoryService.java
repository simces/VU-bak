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
        List<CategoryDAO> topLevelCategories = categoryRepository.findTopLevelCategories();

        List<CategoryWithCountDTO> categoryWithCountDTOs = topLevelCategories.stream()
                .map(this::mapToCategoryWithCountDTO)
                .sorted(Comparator.comparingInt(CategoryWithCountDTO::getPhotoCount).reversed())
                .collect(Collectors.toList());

        int start = Math.min(page * size, categoryWithCountDTOs.size());
        int end = Math.min((page + 1) * size, categoryWithCountDTOs.size());

        return categoryWithCountDTOs.subList(start, end);
    }

    public Object getPhotosOrSubcategories(String categoryName, int page, int size) {
        CategoryDAO category = findCategoryByName(categoryName);
        List<CategoryDAO> subcategories = categoryRepository.findSubcategories(category);

        if (subcategories.isEmpty()) {
            return getAllPhotosByCategory(categoryName, page, size);
        } else {
            List<CategoryWithCountDTO> results = new ArrayList<>();
            int totalPhotoCount = countPhotosInCategory(category);
            results.add(new CategoryWithCountDTO("View All", totalPhotoCount)); // Add "View All" card
            results.addAll(subcategories.stream()
                    .map(this::mapToCategoryWithCountDTO)
                    .sorted(Comparator.comparingInt(CategoryWithCountDTO::getPhotoCount).reversed())
                    .collect(Collectors.toList()));

            int start = Math.min(page * size, results.size());
            int end = Math.min((page + 1) * size, results.size());

            return results.subList(start, end);
        }
    }

    public List<SimplePhotoDTO> getAllPhotosByCategory(String categoryName, int page, int size) {
        CategoryDAO category = findCategoryByName(categoryName);
        List<Long> categoryIds = getCategoryIdsIncludingSubcategories(category);
        Pageable pageable = PageRequest.of(page, size);
        Page<PhotoTagDAO> photoTags = photoTagRepository.findByCategoryIdIn(categoryIds, pageable);
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