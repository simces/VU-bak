package com.photo.business.service.impl;

import com.photo.business.mappers.CommentMapper;
import com.photo.business.repository.CommentRepository;
import com.photo.business.repository.PhotoRepository;
import com.photo.business.repository.UserRepository;
import com.photo.business.repository.model.CommentDAO;
import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.repository.model.UserDAO;
import com.photo.business.service.CommentService;
import com.photo.model.comments.CommentDTO;
import com.photo.model.comments.CommentDetailDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final PhotoRepository photoRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, CommentMapper commentMapper, PhotoRepository photoRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
        this.photoRepository = photoRepository;
    }

    @Override
    public CommentDetailDTO addCommentToPhoto(CommentDTO commentDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get username from auth

        UserDAO user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        PhotoDAO photo = photoRepository.findById(commentDTO.getPhotoId())
                .orElseThrow(() -> new EntityNotFoundException("Photo not found with id: " + commentDTO.getPhotoId()));

        CommentDAO comment = commentMapper.toEntity(commentDTO);
        comment.setUser(user);
        comment.setPhoto(photo);
        comment.setCommentedAt(new Timestamp(System.currentTimeMillis()));
        CommentDAO savedComment = commentRepository.save(comment);

        CommentDetailDTO savedCommentDetailDTO = commentMapper.toDetailDTO(savedComment);
        savedCommentDetailDTO.setUsername(user.getUsername());

        return savedCommentDetailDTO;
    }

    @Override
    public List<CommentDetailDTO> getCommentsByPhotoId(Long photoId) {
        List<CommentDAO> comments = commentRepository.findByPhotoId(photoId);
        return comments.stream()
                .map(comment -> {
                    CommentDetailDTO dto = commentMapper.toDetailDTO(comment);
                    dto.setUsername(comment.getUser().getUsername());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}