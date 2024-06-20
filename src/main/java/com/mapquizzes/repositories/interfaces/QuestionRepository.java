package com.mapquizzes.repositories.interfaces;

import com.mapquizzes.models.entities.QuestionEntity;

import java.util.Optional;

public interface QuestionRepository {

    Optional<QuestionEntity> findById(Integer questionId);
}
