package com.mapquizzes.repositories.interfaces;

import com.mapquizzes.models.entities.QuizQuestionEntity;

import java.util.stream.Stream;

public interface QuizQuestionRepository {

    Stream<QuizQuestionEntity> findAllByQuizId(Integer quizId);
}
