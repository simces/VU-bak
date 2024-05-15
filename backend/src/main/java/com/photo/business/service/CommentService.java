package com.photo.business.service;

import com.photo.business.repository.model.CommentDAO;
import com.photo.model.comments.CommentDTO;
import com.photo.model.comments.CommentDetailDTO;

import java.util.List;

public interface CommentService {

    CommentDetailDTO addCommentToPhoto(CommentDTO commentDTO);

    List<CommentDetailDTO> getCommentsByPhotoId(Long photoId);

}
