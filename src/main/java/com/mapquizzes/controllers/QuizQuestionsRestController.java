package com.mapquizzes.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.mapquizzes.models.dto.QuizDto;
import com.mapquizzes.models.dto.views.QuizViews;
import com.mapquizzes.services.interfaces.QuizQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/quizzes-questions")
@RequiredArgsConstructor
public class QuizQuestionsRestController {

    private final QuizQuestionService service;

    @JsonView(QuizViews.WithQuestions.class)
    @GetMapping("/{quizId}")
    public ResponseEntity<QuizDto> getQuizWithQuestions(@PathVariable Integer quizId) {
        return ResponseEntity.ok(service.getQuizByIdWithQuestions(quizId));
    }

    @JsonView(QuizViews.WithQuestions.class)
    @GetMapping("/{quizId}/mistakes")
    public ResponseEntity<QuizDto> getMistakesQuizWithQuestions(@PathVariable Integer quizId, Principal principal) {
        return ResponseEntity.ok(service.getMistakesQuizByIdWithQuestions(quizId, principal));
    }
}
