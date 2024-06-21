package com.mapquizzes.services.implementations;

import com.mapquizzes.config.CacheConfig;
import com.mapquizzes.models.entities.QuestionEntity;
import com.mapquizzes.repositories.interfaces.QuestionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class QuestionRepositoryImpl implements QuestionRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Cacheable(value = CacheConfig.QUESTION_CACHE_NAME, unless = "#result == null")
    public Optional<QuestionEntity> findById(Integer questionId) {
        return Optional.ofNullable(em.find(QuestionEntity.class, questionId));
    }
}
