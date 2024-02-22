package com.photo.business.service.impl;

import com.photo.business.repository.CommentRepository;
import com.photo.business.repository.model.CommentDAO;
import com.photo.business.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public CommentDAO addCommentToPhoto(CommentDAO comment) {
        comment.setCommentedAt(new Timestamp(System.currentTimeMillis()));
        return commentRepository.save(comment);
    }

    @Override
    public List<CommentDAO> getCommentsByPhotoId(Long photoId) {
        return commentRepository.findByPhotoId(photoId);
    }
}
