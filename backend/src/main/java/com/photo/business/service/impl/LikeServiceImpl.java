package com.photo.business.service.impl;

import com.photo.business.mappers.LikeMapper;
import com.photo.business.repository.LikeRepository;
import com.photo.business.repository.PhotoRepository;
import com.photo.business.repository.UserRepository;
import com.photo.business.repository.model.LikeDAO;
import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.repository.model.UserDAO;
import com.photo.business.service.LikeService;
import com.photo.model.likes.LikeDTO;
import com.photo.model.likes.LikeDetailDTO;
import com.photo.model.likes.LikeStatusDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final LikeMapper likeMapper;


    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository, PhotoRepository photoRepository, UserRepository userRepository, LikeMapper likeMapper) {
        this.likeRepository = likeRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
        this.likeMapper = likeMapper;
    }


    @Override
    public LikeDTO likePhoto(Long photoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserDAO user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        PhotoDAO photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found with id: " + photoId));

        LikeDAO newLike = new LikeDAO();
        newLike.setPhoto(photo);
        newLike.setUser(user);

        LikeDAO savedLike = likeRepository.save(newLike);

        return likeMapper.toDTO(savedLike);
    }

    @Override
    public void unlikePhoto(Long likeId) {
        LikeDAO like = likeRepository.findById(likeId)
                .orElseThrow(() -> new EntityNotFoundException("Like not found with id: " + likeId));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        if (!like.getUser().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("User not authorized to perform this operation");
        }

        likeRepository.delete(like);
    }

    @Override
    public LikeStatusDTO checkLikeStatus(Long photoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserDAO user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Optional<LikeDAO> likeOpt = likeRepository.findByPhotoIdAndUserId(photoId, user.getId());
        return likeOpt.map(likeDAO -> new LikeStatusDTO(true, likeDAO.getId())).orElseGet(() -> new LikeStatusDTO(false, null));
    }

    @Override
    public LikeDetailDTO getLikeDetails(Long photoId) {
        List<UserDAO> users = likeRepository.findUsersByLikedPhoto(photoId);
        List<String> usernames = users.stream()
                .map(UserDAO::getUsername)
                .collect(Collectors.toList());
        int likeCount = usernames.size();

        return new LikeDetailDTO(likeCount, usernames);
    }
}