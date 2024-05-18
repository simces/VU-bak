package com.photo.business.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.photo.business.repository.CategoryRepository;
import com.photo.business.repository.PhotoTagRepository;
import com.photo.business.repository.TagRepository;
import com.photo.business.repository.model.CategoryDAO;
import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.repository.model.PhotoTagDAO;
import com.photo.business.repository.model.TagDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PhotoTaggingService {

    private static final Logger logger = LogManager.getLogger(PhotoTaggingService.class);

    private final WebClient webClient;
    private final TagRepository tagRepository;
    private final PhotoTagRepository photoTagRepository;
    private final ObjectMapper objectMapper;
    private final CategoryRepository categoryRepository;

    @Autowired
    public PhotoTaggingService(WebClient.Builder webClientBuilder, TagRepository tagRepository,
                               PhotoTagRepository photoTagRepository, ObjectMapper objectMapper,
                               CategoryRepository categoryRepository) {
        this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:5000").build();
        this.tagRepository = tagRepository;
        this.photoTagRepository = photoTagRepository;
        this.objectMapper = objectMapper;
        this.categoryRepository = categoryRepository;
    }

    public void tagPhoto(PhotoDAO photo) {

        webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/predict")
                        .queryParam("url", photo.getImageUrl())
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(response -> {
                    try {
                        JsonNode jsonResponse = objectMapper.readTree(response);

                        if (jsonResponse.isObject()) {
                            String label = jsonResponse.get("label").asText();
                            double probability = jsonResponse.get("probability").asDouble();
                            List<String> hierarchicalPath = objectMapper.convertValue(
                                    jsonResponse.get("hierarchical_path"), new TypeReference<>() {});

                            saveTag(label, probability, photo, hierarchicalPath);
                        } else {
                            logger.warn("Received response is not an object for photo ID: {}", photo.getId());
                        }
                    } catch (JsonProcessingException e) {
                        logger.error("Error parsing the response for photo ID: {}", photo.getId(), e);
                    }
                }, error -> logger.error("Error during webClient call for photo ID: {}", photo.getId(), error));
    }

    private void saveTag(String label, double probability, PhotoDAO photo, List<String> hierarchicalPath) {
        TagDAO tag = tagRepository.findByName(label)
                .orElseGet(() -> tagRepository.save(new TagDAO(label, hierarchicalPath)));

        final CategoryDAO[] parent = {null};
        for (String categoryName : hierarchicalPath) {
            CategoryDAO category = categoryRepository.findByNameAndParent(categoryName, parent[0])
                    .orElseGet(() -> {
                        CategoryDAO newCategory = new CategoryDAO();
                        newCategory.setName(categoryName);
                        newCategory.setParent(parent[0]);
                        return categoryRepository.save(newCategory);
                    });
            parent[0] = category;
        }

        CategoryDAO finalCategory = categoryRepository.findByNameAndParent(label, parent[0])
                .orElseGet(() -> {
                    CategoryDAO newCategory = new CategoryDAO();
                    newCategory.setName(label);
                    newCategory.setParent(parent[0]);
                    return categoryRepository.save(newCategory);
                });

        PhotoTagDAO photoTag = new PhotoTagDAO();
        photoTag.setPhotoId(photo.getId());
        photoTag.setTagId(tag.getId());
        photoTag.setConfidence(BigDecimal.valueOf(probability));
        photoTag.setTag(tag);
        photoTag.setPhoto(photo);
        photoTag.setCategory(finalCategory);
        photoTagRepository.save(photoTag);
    }
}