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

    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;
    private final UserRepository userRepository;
    private final AmazonS3 s3client;

    @Autowired
    public PhotoServiceImpl(PhotoRepository photoRepository, PhotoMapper photoMapper, UserRepository userRepository, AmazonS3 s3client) {
        this.photoRepository = photoRepository;
        this.photoMapper = photoMapper;
        this.userRepository = userRepository;
        this.s3client = s3client;
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
    public void uploadPhotoFile(PhotoDTO photoDTO, MultipartFile file) throws IOException {
        String fileUrl = uploadFileToS3(file);
        photoDTO.setImageUrl(fileUrl);

        // Retrieve the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        UserDAO currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        photoDTO.setUserId(currentUser.getId()); // Set the user ID
        photoDTO.setUploadedAt(new Timestamp(System.currentTimeMillis()));

        PhotoDAO photo = photoMapper.photoDTOToPhotoDAO(photoDTO);
        photoRepository.save(photo);
    }

    private String generateFileName(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = Optional.of(originalFileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(originalFileName.lastIndexOf(".") + 1))
                .orElse("");
        return UUID.randomUUID().toString() + "." + fileExtension;
    }


    private String uploadFileToS3(MultipartFile file) throws IOException {
        String fileName = generateFileName(file);
        String bucketName = "photo-ai-bak";
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), new ObjectMetadata()));
        return s3client.getUrl(bucketName, fileName).toString(); // URL of the uploaded file
    }
}