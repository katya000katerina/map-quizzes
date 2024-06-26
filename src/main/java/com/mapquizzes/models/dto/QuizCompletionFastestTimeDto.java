package com.mapquizzes.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizCompletionFastestTimeDto {
    private UserDto user;
    private QuizDto quiz;
    @NotNull
    private Integer timeInMillis;
}
