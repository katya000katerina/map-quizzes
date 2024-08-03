package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.entities.QuestionEntity;

public interface QuestionService {
    QuestionEntity getEntityById(Integer questionId);
}
