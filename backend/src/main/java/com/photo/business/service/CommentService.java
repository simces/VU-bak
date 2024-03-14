package com.photo.business.service;

import com.photo.business.repository.model.CommentDAO;

import java.util.List;

public interface CommentService {

    List<CommentDAO> getCommentsByPhotoId(Long photoId);

    CommentDAO addCommentToPhoto(CommentDAO comment);
}
