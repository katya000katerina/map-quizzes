package com.mapquizzes.repositories.interfaces;

import com.mapquizzes.models.entities.QuizCompletionFastestTimeEntity;

import java.util.Optional;

public interface QuizCompletionFastestTimeRepository {
    void saveOrUpdate(QuizCompletionFastestTimeEntity entity);

    Optional<QuizCompletionFastestTimeEntity> getByUserId(Integer userId);
}
