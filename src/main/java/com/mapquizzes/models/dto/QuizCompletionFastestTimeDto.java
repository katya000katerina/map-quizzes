package com.mapquizzes.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizCompletionFastestTimeDto {
    private UserDto user;
    private QuizDto quiz;
    @NotNull
    private Integer timeInMillis;
}
