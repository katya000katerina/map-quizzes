package com.mapquizzes.models.entities.compositekeys;

import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.models.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class FastestTimeId implements Serializable {
    private UserEntity user;
    private QuizEntity quiz;
}
