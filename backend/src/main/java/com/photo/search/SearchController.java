package com.photo.search;

import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.repository.model.TagDAO;
import com.photo.business.repository.model.UserDAO;
import com.photo.model.photos.EssentialPhotoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/users")
    public List<UserDAO> searchUsersByUsername(@RequestParam String username) {
        return searchService.searchUsersByUsername(username);
    }

    @GetMapping("/photos")
    public List<PhotoDAO> searchPhotosByTitleOrDescription(@RequestParam String searchTerm) {
        return searchService.searchPhotosByTitleOrDescription(searchTerm);
    }

    @GetMapping("/tags")
    public List<PhotoDAO> searchPhotosByTagName(@RequestParam String tagName) {
        return searchService.searchPhotosByTagName(tagName);
    }
}