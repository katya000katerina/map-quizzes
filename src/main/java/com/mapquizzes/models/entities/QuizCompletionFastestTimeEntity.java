package com.mapquizzes.models.entities;

import com.mapquizzes.models.entities.compositekeys.QuizCompletionFastestTimeId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(schema = "quizzes", name = "quizzes_completion_fastest_time")
@IdClass(QuizCompletionFastestTimeId.class)
public class QuizCompletionFastestTimeEntity {
    @Id
    @Column(name = "user_id")
    @NotNull
    private Integer userId;
    @Id
    @Column(name = "quiz_id")
    @NotNull
    private Integer quizId;
    @Column(name = "time_millis")
    @NotNull
    private Integer timeInMillis;
}
