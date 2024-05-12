package com.photo.business.service.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.photo.business.repository.CommentRepository;
import com.photo.business.repository.PhotoRepository;
import com.photo.business.repository.UserRepository;
import com.photo.business.repository.model.CommentDAO;
import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.repository.model.UserDAO;
import com.photo.business.service.AdminService;
import com.photo.business.service.AuditService;
import com.photo.model.CommentDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final AuditService auditService;
    private final PhotoRepository photoRepository;
    private final CommentRepository commentRepository;

    public AdminServiceImpl(UserRepository userRepository, AuditService auditService, PhotoRepository photoRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.auditService = auditService;
        this.photoRepository = photoRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<UserDAO> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDAO getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserDAO updateUser(Long id, UserDAO userDetails) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDAO currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + authentication.getName()));

        UserDAO existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        String dataBefore = convertToJson(existingUser);

        existingUser.setUsername(userDetails.getUsername());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setProfilePictureUrl(userDetails.getProfilePictureUrl());
        existingUser.setBio(userDetails.getBio());
        existingUser.setRole(userDetails.getRole());
        existingUser.setUpdatedAt(Timestamp.from(Instant.now()));

        UserDAO updatedUser = userRepository.save(existingUser);

        String dataAfter = convertToJson(updatedUser);

        auditService.logChange(currentUser.getId(), "UPDATE", "users", existingUser.getId(), dataBefore, dataAfter);

        return updatedUser;
    }

    private String convertToJson(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON: " + e.getMessage(), e);
        }
    }


    @JsonIgnoreProperties("password")  // ignore password field
    abstract static class UserDAOMixin {
    }

    @Override
    public void deleteUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDAO currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + authentication.getName()));

        UserDAO userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        String dataBefore = convertToJson(userToDelete);

        userRepository.delete(userToDelete);

        auditService.logChange(currentUser.getId(), "DELETE", "users", userToDelete.getId(), dataBefore, "{}");

    }

    @Override
    public List<PhotoDAO> getAllPhotos() {
        return photoRepository.findAll();
    }

    @Override
    public PhotoDAO updatePhoto(Long id, PhotoDAO photoDetails) {
        // Authenticate current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDAO currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + authentication.getName()));

        // Find the photo to be updated
        PhotoDAO existingPhoto = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found with id: " + id));

        // Store the data before changes
        String dataBefore = convertToJson(existingPhoto);

        // Update the fields that are allowed to be changed
        existingPhoto.setTitle(photoDetails.getTitle());
        existingPhoto.setDescription(photoDetails.getDescription());

        // Save the updated photo
        PhotoDAO updatedPhoto = photoRepository.save(existingPhoto);

        // Store the data after changes
        String dataAfter = convertToJson(updatedPhoto);

        // Log the update action
        auditService.logChange(currentUser.getId(), "UPDATE", "photos", existingPhoto.getId(), dataBefore, dataAfter);

        return updatedPhoto;
    }

    // Delete Photo Method
    @Override
    public void deletePhoto(Long id) {
        // Authenticate current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDAO currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + authentication.getName()));

        // Find the photo to be deleted
        PhotoDAO photoToDelete = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found with id: " + id));

        // Store the data before deletion
        String dataBefore = convertToJson(photoToDelete);

        // Delete the photo
        photoRepository.delete(photoToDelete);

        // Log the delete action
        auditService.logChange(currentUser.getId(), "DELETE", "photos", photoToDelete.getId(), dataBefore, "{}");
    }

    public List<CommentDTO> getAllComments() {
        return commentRepository.findAll().stream()
                .map(this::toCommentDTO)
                .collect(Collectors.toList());
    }

    private CommentDTO toCommentDTO(CommentDAO comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setUserId(comment.getUserId());
        dto.setPhotoId(comment.getPhotoId());
        dto.setComment(comment.getComment());
        dto.setCommentedAt(comment.getCommentedAt().toLocalDateTime());
        return dto;
    }


    @Override
    @Transactional
    public void deleteComment(Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDAO currentUser = userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + authentication.getName()));

            CommentDAO commentToDelete = commentRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));

            CommentDTO commentToDeleteDTO = toCommentDTO(commentToDelete);
            String dataBefore = convertToJson(commentToDeleteDTO);

            commentRepository.delete(commentToDelete);
            auditService.logChange(currentUser.getId(), "DELETE", "comments", commentToDelete.getId(), dataBefore, "{}");
        } catch (Exception e) {
            System.err.println("Error during comment deletion: " + e.getMessage());
            throw e;
        }
    }
}
