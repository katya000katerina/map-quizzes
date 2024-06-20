package com.mapquizzes.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MistakeDto {
    private Integer userId;
    private Integer questionId;
    @NotNull
    private Integer numberOfMistakes;
}
