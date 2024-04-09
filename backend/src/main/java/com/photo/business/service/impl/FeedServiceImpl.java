package com.photo.business.service.impl;

import com.photo.business.mappers.PhotoMapper;
import com.photo.business.repository.FollowRepository;
import com.photo.business.repository.PhotoRepository;
import com.photo.business.repository.UserRepository;
import com.photo.business.repository.model.FollowDAO;
import com.photo.business.repository.model.UserDAO;
import com.photo.business.service.FeedService;
import com.photo.business.service.TagService;
import com.photo.business.service.UserService;
import com.photo.model.PhotoDTO;
import com.photo.model.PhotoResponseDTO;
import com.photo.model.TagDTO;
import com.photo.model.UserProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedServiceImpl implements FeedService {

    private final FollowRepository followRepository;

    private final PhotoRepository photoRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final TagService tagService;

    private final PhotoMapper photoMapper;

    public FeedServiceImpl(FollowRepository followRepository, PhotoRepository photoRepository, UserRepository userRepository, UserService userService, TagService tagService, PhotoMapper photoMapper) {
        this.followRepository = followRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.tagService = tagService;
        this.photoMapper = photoMapper;
    }

    @Override
    public Page<PhotoResponseDTO> getFeed(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDAO currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        List<Long> followingIds = followRepository.findFollowingsOfUser(currentUser.getId())
                .stream()
                .map(FollowDAO::getFollowingId)
                .collect(Collectors.toList());

        return photoRepository.findByUserIdInOrderByUploadedAtDesc(followingIds, pageable)
                .map(photo -> {
                    UserProfileDTO userProfileDTO = userService.getUserById(photo.getUserId());
                    TagDTO tagDTO = tagService.getTagByPhotoId(photo.getId());
                    PhotoDTO photoDTO = photoMapper.photoDAOToPhotoDTO(photo);

                    return new PhotoResponseDTO(photoDTO, userProfileDTO, tagDTO);
                });
    }
}

