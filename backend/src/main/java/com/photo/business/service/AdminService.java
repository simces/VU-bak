package com.photo.business.service;

import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.repository.model.UserDAO;
import com.photo.model.PhotoDTO;
import com.photo.model.PhotoUpdateDTO;
import com.photo.model.UserDTO;
import com.photo.model.UserUpdateDTO;
import com.photo.model.comments.CommentDTO;
import com.photo.model.comments.CommentDetailDTO;

import java.util.List;

public interface AdminService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    UserDTO updateUser(Long id, UserUpdateDTO userDetails);

    void deleteUser(Long id);

    List<PhotoDTO> getAllPhotos();

    PhotoDTO updatePhoto(Long id, PhotoUpdateDTO photoDetails);

    void deletePhoto(Long id);

    List<CommentDetailDTO> getAllComments();

    void deleteComment(Long id);

}