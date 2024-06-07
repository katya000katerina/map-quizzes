package com.mapquizzes.models.entities.compositekeys;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class QuizCompletionFastestTimeId implements Serializable {
    private Integer userId;
    private Integer quizId;
}
