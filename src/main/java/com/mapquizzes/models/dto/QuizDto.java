package com.mapquizzes.models.dto;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.mapquizzes.models.dto.views.QuizViews;
import lombok.*;

import java.io.Serializable;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto implements Serializable {
    @JsonView(QuizViews.WithoutQuestions.class)
    private Integer id;
    @JsonView(QuizViews.WithoutQuestions.class)
    private String name;
    @JsonView(QuizViews.WithQuestions.class)
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<QuestionDto> questions;
}
