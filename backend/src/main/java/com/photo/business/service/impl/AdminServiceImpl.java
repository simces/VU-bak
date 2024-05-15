package com.photo.business.service.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.photo.business.mappers.CommentMapper;
import com.photo.business.mappers.PhotoMapper;
import com.photo.business.mappers.UserMapper;
import com.photo.business.repository.*;
import com.photo.business.repository.model.CommentDAO;
import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.repository.model.UserDAO;
import com.photo.business.service.AdminService;
import com.photo.business.service.AuditService;
import com.photo.model.photos.FullPhotoDTO;
import com.photo.model.photos.PhotoDTO;
import com.photo.model.photos.PhotoUpdateDTO;
import com.photo.model.users.UserDTO;
import com.photo.model.users.UserUpdateDTO;
import com.photo.model.comments.CommentDetailDTO;
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
    private final UserMapper userMapper;
    private final PhotoMapper photoMapper;
    private final CommentMapper commentMapper;
    private final PhotoTagRepository photoTagRepository;
    private final LikeRepository likeRepository;

    public AdminServiceImpl(UserRepository userRepository, AuditService auditService, PhotoRepository photoRepository, CommentRepository commentRepository, UserMapper userMapper, PhotoMapper photoMapper, CommentMapper commentMapper, PhotoTagRepository photoTagRepository, LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.auditService = auditService;
        this.photoRepository = photoRepository;
        this.commentRepository = commentRepository;
        this.userMapper = userMapper;
        this.photoMapper = photoMapper;
        this.commentMapper = commentMapper;
        this.photoTagRepository = photoTagRepository;
        this.likeRepository = likeRepository;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userDAOToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        UserDAO userDAO = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.userDAOToUserDTO(userDAO);
    }

    @Override
    public UserDTO updateUser(Long id, UserUpdateDTO userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDAO currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + authentication.getName()));

        UserDAO existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        String dataBefore = convertToJson(userMapper.userDAOToAuditUserDTO(existingUser));

        existingUser.setUsername(userDetails.getUsername());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setProfilePictureUrl(userDetails.getProfilePictureUrl());
        existingUser.setBio(userDetails.getBio());
        existingUser.setRole(userDetails.getRole());
        existingUser.setUpdatedAt(Timestamp.from(Instant.now()));

        UserDAO updatedUser = userRepository.save(existingUser);

        String dataAfter = convertToJson(userMapper.userDAOToAuditUserDTO(updatedUser));

        auditService.logChange(currentUser.getId(), "UPDATE", "users", existingUser.getId(), dataBefore, dataAfter);

        return userMapper.userDAOToUserDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDAO currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + authentication.getName()));

        UserDAO userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        String dataBefore = convertToJson(userMapper.userDAOToAuditUserDTO(userToDelete));

        userRepository.delete(userToDelete);

        auditService.logChange(currentUser.getId(), "DELETE", "users", userToDelete.getId(), dataBefore, "{}");
    }

    @Override
    public List<FullPhotoDTO> getAllPhotos() {
        return photoRepository.findAll().stream()
                .map(photoMapper::photoDAOToFullPhotoDTO)
                .collect(Collectors.toList());
    }


    @Override
    public PhotoDTO updatePhoto(Long id, PhotoUpdateDTO photoDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDAO currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + authentication.getName()));

        PhotoDAO existingPhoto = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found with id: " + id));

        String dataBefore = convertToJson(existingPhoto);

        existingPhoto.setTitle(photoDetails.getTitle());
        existingPhoto.setDescription(photoDetails.getDescription());

        PhotoDAO updatedPhoto = photoRepository.save(existingPhoto);

        String dataAfter = convertToJson(updatedPhoto);

        auditService.logChange(currentUser.getId(), "UPDATE", "photos", existingPhoto.getId(), dataBefore, dataAfter);

        return photoMapper.photoDAOToPhotoDTO(updatedPhoto);
    }

    @Transactional
    @Override
    public void deletePhoto(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDAO currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + authentication.getName()));

        PhotoDAO photoToDelete = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found with id: " + id));

        String dataBefore = convertToJson(photoToDelete);

        // Delete related likes and tags first to avoid constraint violations
        likeRepository.deleteByPhotoId(photoToDelete.getId());
        photoTagRepository.deleteByPhotoId(photoToDelete.getId());
        commentRepository.deleteByPhotoId(photoToDelete.getId());

        photoRepository.delete(photoToDelete);

        auditService.logChange(currentUser.getId(), "DELETE", "photos", photoToDelete.getId(), dataBefore, "{}");
    }
    @Override
    public List<CommentDetailDTO> getAllComments() {
        return commentRepository.findAll().stream()
                .map(comment -> {
                    CommentDetailDTO dto = commentMapper.toDetailDTO(comment);
                    dto.setUsername(comment.getUser().getUsername());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDAO currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + authentication.getName()));

        CommentDAO commentToDelete = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));

        CommentDetailDTO commentToDeleteDTO = commentMapper.toDetailDTO(commentToDelete);
        commentToDeleteDTO.setUsername(commentToDelete.getUser().getUsername());
        String dataBefore = convertToJson(commentToDeleteDTO);

        commentRepository.delete(commentToDelete);
        auditService.logChange(currentUser.getId(), "DELETE", "comments", commentToDelete.getId(), dataBefore, "{}");
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
}