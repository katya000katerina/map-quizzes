package com.mapquizzes.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Collection;

@Entity
@Table(schema = "quizzes", name = "quizzes")
@Getter
public class QuizEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    @NotNull @NotBlank
    private String name;
    @OneToMany(mappedBy = "quizEntity", fetch = FetchType.LAZY)
    private Collection<QuizQuestionEntity> quizQuestions;
}
