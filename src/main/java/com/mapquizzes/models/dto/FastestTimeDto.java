package com.mapquizzes.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FastestTimeDto {
    private UserDto user;
    @JsonIgnore
    private QuizDto quiz;
    @NotNull
    private Integer timeInMillis;
}
