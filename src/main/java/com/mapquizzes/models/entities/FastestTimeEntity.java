package com.mapquizzes.models.entities;

import com.mapquizzes.models.entities.compositekeys.FastestTimeId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "quizzes", name = "quizzes_completion_fastest_time")
@IdClass(FastestTimeId.class)
public class FastestTimeEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    private QuizEntity quiz;
    @Column(name = "time_millis")
    private Integer timeInMillis;

}
