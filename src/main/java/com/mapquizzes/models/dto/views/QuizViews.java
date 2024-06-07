package com.mapquizzes.models.dto.views;

public interface QuizViews {
    interface WithoutQuestions{}
    interface WithQuestions extends WithoutQuestions{}
}
