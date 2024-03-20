package com.photo.business.service.impl;

import com.photo.business.repository.LikeRepository;
import com.photo.business.repository.PhotoRepository;
import com.photo.business.repository.UserRepository;
import com.photo.business.repository.model.LikeDAO;
import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.repository.model.UserDAO;
import com.photo.business.service.LikeService;
import com.photo.model.LikeStatusDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    private final PhotoRepository photoRepository;

    private final UserRepository userRepository;

    public LikeServiceImpl(LikeRepository likeRepository, PhotoRepository photoRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public LikeDAO likePhoto(Long photoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get username from authentication object

        UserDAO user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        PhotoDAO photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found with id: " + photoId));

        // Create a new like
        LikeDAO newLike = new LikeDAO();
        newLike.setPhoto(photo);
        newLike.setUser(user);

        return likeRepository.save(newLike);
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
        if (likeOpt.isPresent()) {
            return new LikeStatusDTO(true, likeOpt.get().getId());
        } else {
            return new LikeStatusDTO(false, null);
        }
    }
}

