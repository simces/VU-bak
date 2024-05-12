package com.photo.business.service.impl;

import com.photo.business.repository.CommentRepository;
import com.photo.business.repository.UserRepository;
import com.photo.business.repository.model.CommentDAO;
import com.photo.business.repository.model.UserDAO;
import com.photo.business.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

    @Service
    public class CommentServiceImpl implements CommentService {

        private final CommentRepository commentRepository;

        private final UserRepository userRepository;

        public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository) {
            this.commentRepository = commentRepository;
            this.userRepository = userRepository;
        }

        @Override
        public CommentDAO addCommentToPhoto(CommentDAO comment) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName(); // Get username from auth

            UserDAO user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

            comment.setUserId(user.getId());
            comment.setCommentedAt(new Timestamp(System.currentTimeMillis()));
            return commentRepository.save(comment);
        }

    @Override
    public List<CommentDAO> getCommentsByPhotoId(Long photoId) {
        return commentRepository.findByPhotoId(photoId);
    }
}

