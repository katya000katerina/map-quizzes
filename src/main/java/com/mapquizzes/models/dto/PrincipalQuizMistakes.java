package com.mapquizzes.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrincipalQuizMistakes {
    private Integer quizId;
    private String quizName;
    private List<PrincipalMistake> mistakes;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PrincipalMistake {
        private String question;
        private Integer numberOfMistakes;
    }
}