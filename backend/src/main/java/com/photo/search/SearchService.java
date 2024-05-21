package com.photo.search;

import com.photo.business.repository.model.PhotoDAO;
import com.photo.business.repository.model.UserDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<UserDAO> searchUsersByUsername(String username) {
        String sql = "SELECT * FROM users WHERE levenshtein(username, :username) <= 2 OR username LIKE CONCAT('%', :username, '%')";
        Query query = entityManager.createNativeQuery(sql, UserDAO.class);
        query.setParameter("username", username);
        return query.getResultList();
    }

    public List<PhotoDAO> searchPhotosByTitleOrDescription(String searchTerm) {
        String sql = "SELECT * FROM photos WHERE (levenshtein(title, :searchTerm) <= 2 OR title LIKE CONCAT('%', :searchTerm, '%')) " +
                "OR (levenshtein(description, :searchTerm) <= 2 OR description LIKE CONCAT('%', :searchTerm, '%'))";
        Query query = entityManager.createNativeQuery(sql, PhotoDAO.class);
        query.setParameter("searchTerm", searchTerm);
        return query.getResultList();
    }

    public List<PhotoDAO> searchPhotosByTagName(String tagName) {
        String tagSql = "SELECT id FROM tag WHERE levenshtein(name, :tagName) <= 2 OR name LIKE :tagNamePattern";
        Query tagQuery = entityManager.createNativeQuery(tagSql);
        tagQuery.setParameter("tagName", tagName);
        tagQuery.setParameter("tagNamePattern", "%" + tagName + "%");
        List<Long> tagIds = tagQuery.getResultList();

        if (tagIds.isEmpty()) {
            return List.of();
        }

        String photoSql = "SELECT p.* FROM photos p " +
                "JOIN photo_tag pt ON p.id = pt.photo_id " +
                "WHERE pt.tag_id IN :tagIds";
        Query photoQuery = entityManager.createNativeQuery(photoSql, PhotoDAO.class);
        photoQuery.setParameter("tagIds", tagIds);
        return photoQuery.getResultList();
    }
}
