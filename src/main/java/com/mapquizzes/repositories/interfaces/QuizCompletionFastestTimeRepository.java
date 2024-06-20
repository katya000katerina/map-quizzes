package com.mapquizzes.repositories.interfaces;

import com.mapquizzes.models.entities.QuizCompletionFastestTimeEntity;
import com.mapquizzes.models.entities.UserEntity;

import java.util.Optional;

public interface QuizCompletionFastestTimeRepository {
    void saveOrUpdate(QuizCompletionFastestTimeEntity entity);


    Optional<QuizCompletionFastestTimeEntity> getByUser(UserEntity userEntity);
}
