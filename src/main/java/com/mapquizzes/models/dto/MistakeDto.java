package com.mapquizzes.models.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MistakeDto(@NotNull @Positive Integer questionId,
                         @NotNull @Positive Integer numberOfMistakes) {
}
