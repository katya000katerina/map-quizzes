package com.mapquizzes.models.dto;


import com.fasterxml.jackson.annotation.JsonView;
import com.mapquizzes.models.dto.views.QuizViews;

import java.io.Serializable;
import java.util.Collection;

@JsonView(QuizViews.WithoutQuestions.class)
public record QuizDto(Integer id,
                      String name,
                      @JsonView(QuizViews.WithQuestions.class)
                      Collection<QuestionDto> questions) implements Serializable {
    @JsonView(QuizViews.WithQuestions.class)
    public record QuestionDto(Integer id, String question) implements Serializable {}
}
