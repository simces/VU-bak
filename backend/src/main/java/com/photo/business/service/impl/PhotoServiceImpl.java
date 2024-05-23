package com.photo.business.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.photo.business.mappers.PhotoMapper;
import com.photo.business.mappers.UserMapper;
import com.photo.business.repository.*;
import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.repository.model.UserDAO;
import com.photo.business.repository.model.UserDeviceDAO;
import com.photo.business.service.PhotoService;
import com.photo.business.service.TagService;
import com.photo.model.photos.*;
import com.photo.model.tags.TagDTO;
import com.photo.model.users.UserBasicDetailsDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PhotoServiceImpl implements PhotoService {

    private static final Logger logger = LogManager.getLogger(PhotoServiceImpl.class);

    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;
    private final UserRepository userRepository;
    private final AmazonS3 s3client;
    private final PhotoTaggingService photoTaggingService;
    private final TagService tagService;
    private final UserMapper userMapper;
    private final UserDeviceRepository userDeviceRepository;
    private final GeocodingService geocodingService;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public PhotoServiceImpl(PhotoRepository photoRepository, PhotoMapper photoMapper, UserRepository userRepository, AmazonS3 s3client, PhotoTaggingService photoTaggingService, TagService tagService, UserMapper userMapper, UserDeviceRepository userDeviceRepository, GeocodingService geocodingService, LikeRepository likeRepository, CommentRepository commentRepository) {
        this.photoRepository = photoRepository;
        this.photoMapper = photoMapper;
        this.userRepository = userRepository;
        this.s3client = s3client;
        this.photoTaggingService = photoTaggingService;
        this.tagService = tagService;
        this.userMapper = userMapper;
        this.userDeviceRepository = userDeviceRepository;
        this.geocodingService = geocodingService;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public FullPhotoDTO getFullPhotoById(Long id) {
        PhotoDAO photo = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));

        List<TagDTO> tags = tagService.getTagsByPhotoId(id);
        FullPhotoDTO fullPhotoDTO = photoMapper.photoDAOToFullPhotoDTO(photo);
        fullPhotoDTO.setTags(tags);

        if (fullPhotoDTO.getLatitude() != null && fullPhotoDTO.getLongitude() != null) {
            String location = geocodingService.getLocation(fullPhotoDTO.getLatitude(), fullPhotoDTO.getLongitude());
            fullPhotoDTO.setLocation(location);
        }

        return fullPhotoDTO;
    }

    @Override
    public PhotoResponseDTO getPhotoResponseById(Long id) {
        FullPhotoDTO fullPhotoDTO = getFullPhotoById(id);
        UserBasicDetailsDTO userBasicDetailsDTO = userMapper.userDAOToUserBasicDetailsDTO(
                userRepository.findById(fullPhotoDTO.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found"))
        );
        return new PhotoResponseDTO(fullPhotoDTO, userBasicDetailsDTO);
    }

    @Override
    public List<PhotoDTO> getPhotosByUserId(Long userId) {
        List<PhotoDAO> photos = photoRepository.findByUserId(userId);
        return photos.stream().map(photoMapper::photoDAOToPhotoDTO).collect(Collectors.toList());
    }


    @Transactional
    @Override
    public void uploadPhotoFile(PhotoDTO photoDTO, MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserDAO user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        photoDTO.setUserId(user.getId());

        String fileUrl = uploadFileToS3(file, photoDTO);
        photoDTO.setImageUrl(fileUrl);
        photoDTO.setUploadedAt(new Timestamp(System.currentTimeMillis()));

        PhotoDAO photo = photoMapper.photoDTOToPhotoDAO(photoDTO);

        // Only set the device if deviceId is not null
        if (photoDTO.getDeviceId() != null) {
            logger.info("Device ID provided: {}", photoDTO.getDeviceId());
            UserDeviceDAO device = userDeviceRepository.findById(photoDTO.getDeviceId())
                    .orElseThrow(() -> new IllegalArgumentException("Device not found with id: " + photoDTO.getDeviceId()));
            photo.setDevice(device);
            logger.info("Device set: {} - {}", device.getType(), device.getModel());
        } else {
            logger.info("No device ID provided");
            photo.setDevice(null); // Ensure the device field is explicitly set to null
        }

        // Set latitude and longitude
        photo.setLatitude(photoDTO.getLatitude());
        photo.setLongitude(photoDTO.getLongitude());

        photo = photoRepository.save(photo);
        logger.info("Photo uploaded successfully for user {} with ID {}", photoDTO.getUserId(), photo.getId());

        if (photo.getId() != null) {
            tagPhoto(photo.getId());
        } else {
            logger.warn("Photo ID is null after save operation");
        }
    }


    @Override
    public void tagPhoto(Long photoId) {
        PhotoDAO photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
        photoTaggingService.tagPhoto(photo);
    }

    private String uploadFileToS3(MultipartFile file, PhotoDTO photoDTO) throws IOException {
        String fileName = generateFileName(file);
        String bucketName = "photo-ai-bak";
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), new ObjectMetadata()));
        String fileUrl = s3client.getUrl(bucketName, fileName).toString();

        photoDTO.setImageUrl(fileUrl);

        return fileUrl;
    }

    private String generateFileName(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = Optional.of(originalFileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(originalFileName.lastIndexOf(".") + 1))
                .orElse("");
        return UUID.randomUUID() + "." + fileExtension;
    }

    @Override
    public List<HotPhotoDTO> getHotPhotos(int page, int size) {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);

        List<PhotoDAO> recentPhotos = photoRepository.findRecentPhotos(cutoffTime);

        List<HotPhotoDTO> hotPhotoDTOs = recentPhotos.stream()
                .map(photo -> {
                    Long likeCount = likeRepository.countByPhotoId(photo.getId());
                    return new HotPhotoDTO(photo.getId(), photo.getImageUrl(), likeCount);
                })
                .sorted((p1, p2) -> Long.compare(p2.getLikeCount(), p1.getLikeCount()))
                .collect(Collectors.toList());

        int start = Math.min(page * size, hotPhotoDTOs.size());
        int end = Math.min((page * size) + size, hotPhotoDTOs.size());
        return hotPhotoDTOs.subList(start, end);
    }

    @Override
    public Page<PhotoRankDTO> getTopRankedPhotos(Pageable pageable) {
        List<PhotoDAO> allPhotos = photoRepository.findAll();
        List<PhotoRankDTO> photoRankDTOs = allPhotos.stream().map(photo -> {
            Long likeCount = likeRepository.countByPhotoId(photo.getId());
            Long commentCount = commentRepository.countByPhotoId(photo.getId());
            return new PhotoRankDTO(photo.getId(), photo.getImageUrl(), likeCount, commentCount);
        }).toList();

        List<PhotoRankDTO> sortedPhotoRankDTOs = photoRankDTOs.stream()
                .sorted((a, b) -> {
                    Long aTotal = a.getLikeCount() + a.getCommentCount();
                    Long bTotal = b.getLikeCount() + b.getCommentCount();
                    return bTotal.compareTo(aTotal);
                })
                .collect(Collectors.toList());

        int start = Math.min((int) pageable.getOffset(), sortedPhotoRankDTOs.size());
        int end = Math.min((start + pageable.getPageSize()), sortedPhotoRankDTOs.size());
        List<PhotoRankDTO> paginatedList = sortedPhotoRankDTOs.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, sortedPhotoRankDTOs.size());
    }

    @Override
    public Page<NewPhotoDTO> getNewPhotos(Pageable pageable) {
        Page<PhotoDAO> photos = photoRepository.findAllByOrderByUploadedAtDesc(pageable);
        List<NewPhotoDTO> photoDTOs = photos.stream().map(photo -> new NewPhotoDTO(
                photo.getId(),
                photo.getImageUrl(),
                photo.getUploadedAt()
        )).collect(Collectors.toList());
        return new PageImpl<>(photoDTOs, pageable, photos.getTotalElements());
    }
}