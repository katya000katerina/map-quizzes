package com.mapquizzes.repositories.interfaces;

import com.mapquizzes.models.entities.QuestionEntity;

import java.util.stream.Stream;

public interface QuestionRepository {

    Stream<QuestionEntity> findAllByQuizId(Integer quizId);
}
