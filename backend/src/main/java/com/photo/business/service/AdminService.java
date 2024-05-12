package com.photo.business.service;

import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.repository.model.UserDAO;
import com.photo.model.CommentDTO;

import java.util.List;

public interface AdminService {

    List<UserDAO> getAllUsers();

    UserDAO getUserById(Long id);

    UserDAO updateUser(Long id, UserDAO userDetails);

    void deleteUser(Long id);

    List<PhotoDAO> getAllPhotos();

    PhotoDAO updatePhoto(Long id, PhotoDAO photoDetails);

    void deletePhoto(Long id);

    List<CommentDTO> getAllComments();

    void deleteComment(Long id);

}