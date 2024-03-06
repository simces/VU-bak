package com.photo.business.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.photo.business.mappers.PhotoMapper;
import com.photo.business.repository.PhotoRepository;
import com.photo.business.repository.UserRepository;
import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.repository.model.UserDAO;
import com.photo.business.service.PhotoService;
import com.photo.model.PhotoDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
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

    @Autowired
    public PhotoServiceImpl(PhotoRepository photoRepository, PhotoMapper photoMapper, UserRepository userRepository, AmazonS3 s3client, PhotoTaggingService photoTaggingService) {
        this.photoRepository = photoRepository;
        this.photoMapper = photoMapper;
        this.userRepository = userRepository;
        this.s3client = s3client;
        this.photoTaggingService = photoTaggingService;
    }

    @Override
    public PhotoDTO uploadPhoto(PhotoDTO photoDTO) {
        PhotoDAO photo = photoMapper.photoDTOToPhotoDAO(photoDTO);
        photo.setUploadedAt(new Timestamp(System.currentTimeMillis()));
        PhotoDAO savedPhoto = photoRepository.save(photo);
        return photoMapper.photoDAOToPhotoDTO(savedPhoto);
    }

    @Override
    public List<PhotoDTO> getPhotosByUserId(Long userId) {
        List<PhotoDAO> photos = photoRepository.findByUserId(userId);
        return photos.stream().map(photoMapper::photoDAOToPhotoDTO).collect(Collectors.toList());
    }


    @Override
    public PhotoDTO getPhotoById(Long id) {
        PhotoDAO photo = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
        return photoMapper.photoDAOToPhotoDTO(photo);
    }


    @Override
    @Transactional
    public void uploadPhotoFile(PhotoDTO photoDTO, MultipartFile file) throws IOException {
        logger.info("Starting upload process for user {}", photoDTO.getUserId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDAO user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        photoDTO.setUserId(user.getId());

        String fileUrl = uploadFileToS3(file, photoDTO);
        photoDTO.setImageUrl(fileUrl);
        photoDTO.setUploadedAt(new Timestamp(System.currentTimeMillis()));

        PhotoDAO photo = photoMapper.photoDTOToPhotoDAO(photoDTO);
        photo = photoRepository.save(photo);
        logger.info("Photo uploaded successfully for user {} with ID {}", photoDTO.getUserId(), photo.getId());

        if(photo.getId() != null) {
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

    private String generateFileName(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = Optional.of(originalFileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(originalFileName.lastIndexOf(".") + 1))
                .orElse("");
        return UUID.randomUUID().toString() + "." + fileExtension;
    }


    private String uploadFileToS3(MultipartFile file, PhotoDTO photoDTO) throws IOException {
        String fileName = generateFileName(file);
        String bucketName = "photo-ai-bak";
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), new ObjectMetadata()));
        String fileUrl = s3client.getUrl(bucketName, fileName).toString();

        photoDTO.setImageUrl(fileUrl);

        return fileUrl;
    }

}