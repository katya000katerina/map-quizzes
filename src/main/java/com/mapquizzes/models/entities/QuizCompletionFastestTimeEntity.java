package com.mapquizzes.models.entities;

import com.mapquizzes.models.entities.compositekeys.QuizCompletionFastestTimeId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "quizzes", name = "quizzes_completion_fastest_time")
@IdClass(QuizCompletionFastestTimeId.class)
@Getter
@Setter
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
