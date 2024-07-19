package com.mapquizzes.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FastestTimeDto {
    private UserDto user;
    private QuizDto quiz;
    @NotNull
    private Integer timeInMillis;
}
