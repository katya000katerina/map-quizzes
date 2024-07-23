package com.mapquizzes.models.dto;


import java.util.List;

public record PrincipalQuizMistakes(Integer quizId, String quizName, List<PrincipalMistake> mistakes) {

    public record PrincipalMistake(String question, Integer numberOfMistakes) {}
}