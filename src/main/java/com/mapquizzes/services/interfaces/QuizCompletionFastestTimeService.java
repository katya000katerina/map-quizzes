package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.QuizCompletionFastestTimeDto;

import java.security.Principal;

public interface QuizCompletionFastestTimeService {
    void saveOrUpdate(QuizCompletionFastestTimeDto dto, Principal principal, Integer quizId);
}
