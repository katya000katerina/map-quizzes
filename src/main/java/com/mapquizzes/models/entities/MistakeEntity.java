package com.mapquizzes.models.entities;

import com.mapquizzes.models.entities.compositekeys.MistakeId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "quizzes", name = "mistakes")
@IdClass(MistakeId.class)
public class MistakeEntity {
    @Id
    @Column(name = "user_id")
    @NotNull
    private Integer userId;
    @Id
    @Column(name = "question_id")
    @NotNull
    private Integer questionId;
    @Column(name = "number_of_mistakes")
    @NotNull
    private Integer numberOfMistakes;
}
