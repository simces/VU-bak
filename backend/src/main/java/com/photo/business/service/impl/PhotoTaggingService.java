package com.photo.business.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.photo.business.repository.PhotoTagRepository;
import com.photo.business.repository.TagRepository;
import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.repository.model.PhotoTagDAO;
import com.photo.business.repository.model.TagDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Service
public class PhotoTaggingService {

    private static final Logger logger = LogManager.getLogger(PhotoTaggingService.class);

    private final WebClient webClient;
    private final TagRepository tagRepository;
    private final PhotoTagRepository photoTagRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public PhotoTaggingService(WebClient.Builder webClientBuilder, TagRepository tagRepository, PhotoTagRepository photoTagRepository, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:5000").build();
        this.tagRepository = tagRepository;
        this.photoTagRepository = photoTagRepository;
        this.objectMapper = objectMapper;
    }

    public void tagPhoto(PhotoDAO photo) {
        logger.info("Starting to tag photo with ID: {}", photo.getId());

        webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/predict")
                        .queryParam("url", photo.getImageUrl())
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(response -> {
                    try {
                        JsonNode jsonResponse = objectMapper.readTree(response);
                        if (jsonResponse.isArray() && jsonResponse.has(0)) {
                            JsonNode firstTag = jsonResponse.get(0);
                            String label = firstTag.get("label").asText();
                            double probability = firstTag.get("probability").asDouble();

                            logger.debug("Received tag: {} with probability: {} for photo ID: {}", label, probability, photo.getId());
                            saveTag(label, probability, photo);
                        } else {
                            logger.warn("Received response is not an array or is empty for photo ID: {}", photo.getId());
                        }
                    } catch (JsonProcessingException e) {
                        logger.error("Error parsing the response for photo ID: {}", photo.getId(), e);
                    }
                }, error -> logger.error("Error during webClient call for photo ID: {}", photo.getId(), error));

        logger.info("Tagging process initiated for photo ID: {}", photo.getId());
    }

    private void saveTag(String label, double probability, PhotoDAO photo) {
        TagDAO tag = tagRepository.findByName(label)
                .orElseGet(() -> tagRepository.save(new TagDAO(null, label)));
        PhotoTagDAO photoTag = new PhotoTagDAO();
        photoTag.setPhotoId(photo.getId());
        photoTag.setTagId(tag.getId());
        photoTag.setConfidence(BigDecimal.valueOf(probability));
        photoTag.setTag(tag);
        photoTag.setPhoto(photo);
        photoTagRepository.save(photoTag);
    }
}