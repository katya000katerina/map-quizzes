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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    private QuizEntity quiz;
    @Column(name = "time_millis")
    @NotNull
    private Integer timeInMillis;

}
