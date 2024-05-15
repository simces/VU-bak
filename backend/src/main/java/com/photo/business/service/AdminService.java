package com.photo.business.service;

import com.photo.model.photos.PhotoDTO;
import com.photo.model.photos.PhotoUpdateDTO;
import com.photo.model.users.UserDTO;
import com.photo.model.users.UserUpdateDTO;
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