package com.mapquizzes.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizCompletionFastestTimeDto {
    private Integer userId;
    private Integer quizId;
    @NotNull
    private Integer timeInMillis;
}
