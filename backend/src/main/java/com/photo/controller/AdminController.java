package com.photo.controller;

import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.repository.model.UserDAO;
import com.photo.business.service.AdminService;
import com.photo.model.CommentDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserDAO> getAllUsers() {
        return adminService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UserDAO getUserById(@PathVariable Long id) {
        return adminService.getUserById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public UserDAO updateUser(@PathVariable Long id, @RequestBody UserDAO userDetails) {
        return adminService.updateUser(id, userDetails);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            adminService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/photos")
    public ResponseEntity<List<PhotoDAO>> getAllPhotos() {
        List<PhotoDAO> photos = adminService.getAllPhotos();
        return ResponseEntity.ok(photos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/photos/{id}")
    public ResponseEntity<PhotoDAO> updatePhoto(@PathVariable Long id, @RequestBody PhotoDAO photoDetails) {
        try {
            PhotoDAO updatedPhoto = adminService.updatePhoto(id, photoDetails);
            return ResponseEntity.ok(updatedPhoto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/photos/{id}")
    public ResponseEntity<?> deletePhoto(@PathVariable Long id) {
        try {
            adminService.deletePhoto(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting photo: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/comments")
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        List<CommentDTO> comments = adminService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        try {
            adminService.deleteComment(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting comment: " + e.getMessage());
        }
    }

}