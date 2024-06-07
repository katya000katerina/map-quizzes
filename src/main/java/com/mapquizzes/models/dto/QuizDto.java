package com.mapquizzes.models.dto;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.mapquizzes.models.dto.views.QuizViews;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class QuizDto {
    @JsonView(QuizViews.WithoutQuestions.class)
    private Integer id;
    @JsonView(QuizViews.WithoutQuestions.class)
    private String name;
    @JsonView(QuizViews.WithQuestions.class)
    @JsonManagedReference
    private Collection<QuestionDto> questions;
}
