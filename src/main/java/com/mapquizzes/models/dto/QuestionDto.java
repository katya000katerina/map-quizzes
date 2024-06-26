package com.mapquizzes.models.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.mapquizzes.models.dto.views.QuizViews;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto implements Serializable {
    @JsonView(QuizViews.WithQuestions.class)
    private Integer id;
    @JsonView(QuizViews.WithQuestions.class)
    private String question;
    @JsonView(QuizViews.WithQuestions.class)
    @JsonBackReference
    private QuizDto quiz;
}
