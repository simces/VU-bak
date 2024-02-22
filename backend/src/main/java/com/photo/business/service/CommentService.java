package com.photo.business.service;

import com.photo.business.repository.model.CommentDAO;

import java.util.List;

public interface CommentService {

    public CommentDAO addCommentToPhoto(CommentDAO comment);

    public List<CommentDAO> getCommentsByPhotoId(Long photoId);
}
