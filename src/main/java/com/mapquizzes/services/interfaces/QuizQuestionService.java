package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.QuizDto;

import java.security.Principal;

public interface QuizQuestionService {
    QuizDto getQuizByIdWithQuestions(Integer quizId);
    QuizDto getMistakesQuizByIdWithQuestions(Integer quizId, Principal principal);
}
