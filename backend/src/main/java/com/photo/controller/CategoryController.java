package com.photo.controller;

import com.photo.business.service.impl.CategoryService;
import com.photo.model.CategoryWithCountDTO;
import com.photo.model.photos.SimplePhotoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/top-level")
    public List<CategoryWithCountDTO> getTopLevelCategories(@RequestParam int page, @RequestParam int size) {
        return categoryService.getTopLevelCategoriesWithCounts(page, size);
    }

    @GetMapping("/{parentCategoryName}/subcategories")
    public Object getSubcategories(@PathVariable String parentCategoryName) {
        return categoryService.getPhotosOrSubcategories(parentCategoryName, 0, 10);
    }

    @GetMapping("/{categoryName}/content")
    public List<SimplePhotoDTO> getPhotos(@PathVariable String categoryName,
                                          @RequestParam int page,
                                          @RequestParam int size) {
        return categoryService.getAllPhotosByCategory(categoryName, page, size);
    }

    @GetMapping("/{categoryName}/all-photos")
    public List<SimplePhotoDTO> getAllPhotos(@PathVariable String categoryName,
                                             @RequestParam int page,
                                             @RequestParam int size) {
        return categoryService.getAllPhotosByCategory(categoryName, page, size);
    }
}




