package com.mapquizzes.models.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TestCompletionTimeDto(@NotNull Integer quizId,
                                    @NotNull @Positive Integer timeInMillis) {}
