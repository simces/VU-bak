package com.photo.controller;

import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.service.impl.TestService;
import com.photo.model.photos.SimplePhotoDTO;
import com.photo.model.photos.TestFullPhotoDTO;
import com.photo.model.photos.TestSimplePhotoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// main point of this class is to compare the performance
// between fetching photo using the full DAO, DTO, and a minimized DTO
// this shows the impact that having optimized data actually matters

// also, the latter methods demonstrate the importance of pagination
// since there's no point to fetch a million photos, when you only need 10

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/fullDAO")
    public List<PhotoDAO> getAllPhotosAsDAO() {
        return testService.getAllPhotosAsDAO();
    }

    @GetMapping("/fullDTO")
    public List<TestFullPhotoDTO> getAllPhotosAsFullDTO() {
        return testService.getAllPhotosAsTestPhotoDTO();
    }

    @GetMapping("/smallDTO")
    public List<TestSimplePhotoDTO> getAllPhotosAsSimpleDTO() {
        return testService.getAllPhotosAsSimpleDTO();
    }
}