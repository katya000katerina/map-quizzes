package com.mapquizzes.models.entities.compositekeys;

import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.models.entities.UserEntity;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class QuizCompletionFastestTimeId implements Serializable {
    private UserEntity user;
    private QuizEntity quiz;
}
