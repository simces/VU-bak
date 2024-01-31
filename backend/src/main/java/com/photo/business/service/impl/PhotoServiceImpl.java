package com.photo.business.service.impl;

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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.UUID;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;
    private final UserRepository userRepository;

    @Autowired
    public PhotoServiceImpl(PhotoRepository photoRepository, PhotoMapper photoMapper, UserRepository userRepository) {
        this.photoRepository = photoRepository;
        this.photoMapper = photoMapper;
        this.userRepository = userRepository;
    }

    @Override
    public PhotoDTO uploadPhoto(PhotoDTO photoDTO) {
        PhotoDAO photo = photoMapper.photoDTOToPhotoDAO(photoDTO);
        photo.setUploadedAt(new Timestamp(System.currentTimeMillis()));
        PhotoDAO savedPhoto = photoRepository.save(photo);
        return photoMapper.photoDAOToPhotoDTO(savedPhoto);
    }

    @Override
    public PhotoDTO getPhotoById(Long id) {
        PhotoDAO photo = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
        return photoMapper.photoDAOToPhotoDTO(photo);
    }

    @Override
    public void uploadPhotoFile(PhotoDTO photoDTO, MultipartFile file) throws IOException {
        String fileName = storeFile(file);
        photoDTO.setImageUrl(fileName);

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

    private String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file");
        }

        File directory = new File("C:\\Users\\Simas\\Desktop\\test");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Construct file path
        String originalFileName = file.getOriginalFilename();
        String storedFileName = UUID.randomUUID().toString() + "_" + originalFileName;
        Path destinationFilePath = Paths.get("C:\\Users\\Simas\\Desktop\\test", storedFileName);

        // Save the file
        Files.copy(file.getInputStream(), destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
        return storedFileName;
    }
}
