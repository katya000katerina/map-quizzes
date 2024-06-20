package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.QuizDto;

public interface QuizQuestionService {
    QuizDto getQuizByIdWithQuestions(Integer quizId);
}
