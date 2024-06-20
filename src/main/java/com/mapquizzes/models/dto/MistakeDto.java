package com.mapquizzes.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MistakeDto {
    private UserDto user;
    private QuestionDto question;
    @NotNull
    private Integer numberOfMistakes;
}
