package com.mapquizzes.models.dto;


import java.util.List;

public record PrincipalQuizMistakesDto(Integer quizId, String quizName, List<PrincipalMistakeDto> mistakes) {

    public record PrincipalMistakeDto(String question, Integer numberOfMistakes) {}
}