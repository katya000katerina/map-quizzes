package com.mapquizzes.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Table(schema = "quizzes", name = "quizzes_questions")
@Getter
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "question")
    @NotNull @NotBlank
    private String question;
    @ManyToOne
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    @NotNull
    private QuizEntity quiz;
}
