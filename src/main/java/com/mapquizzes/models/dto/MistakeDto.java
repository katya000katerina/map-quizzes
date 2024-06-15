package com.mapquizzes.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MistakeDto {
    private Integer userId;
    private Integer questionId;
    @NotNull
    private Integer numberOfMistakes;
}
