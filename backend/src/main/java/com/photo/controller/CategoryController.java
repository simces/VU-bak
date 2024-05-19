package com.photo.controller;

import com.photo.business.service.impl.CategoryService;
import com.photo.model.CategoryWithCountDTO;
import com.photo.model.photos.SimplePhotoDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/top-level")
    public List<CategoryWithCountDTO> getTopLevelCategories(@RequestParam int page,
                                                            @RequestParam int size) {
        return categoryService.getTopLevelCategoriesWithCounts(page, size);
    }

    @GetMapping("/{parentCategoryName}/subcategories")
    public Object getSubcategories(@PathVariable String parentCategoryName,
                                   @RequestParam int page,
                                   @RequestParam int size) {
        return categoryService.getPhotosOrSubcategories(parentCategoryName, page, size);
    }

    @GetMapping("/{categoryName}/content")
    public List<SimplePhotoDTO> getPhotos(@PathVariable String categoryName,
                                          @RequestParam int page,
                                          @RequestParam int size) {
        return categoryService.getAllPhotosByCategory(categoryName, page, size);
    }
}