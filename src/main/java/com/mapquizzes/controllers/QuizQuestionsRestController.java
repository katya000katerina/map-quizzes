package com.mapquizzes.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.mapquizzes.models.dto.QuizDto;
import com.mapquizzes.models.dto.views.QuizViews;
import com.mapquizzes.services.interfaces.QuizQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    private final QuizQuestionService quizQuestionService;

    @JsonView(QuizViews.WithQuestions.class)
    @GetMapping(value = "/{quizId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizDto> getQuizWithQuestions(@PathVariable Integer quizId) {
        QuizDto quizDto = quizQuestionService.getQuizByIdWithQuestions(quizId);
        return ResponseEntity.ok(quizDto);
    }

    @JsonView(QuizViews.WithQuestions.class)
    @GetMapping(value = "/{quizId}/mistakes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizDto> getMistakesQuizWithQuestions(@PathVariable Integer quizId, Principal principal) {
        QuizDto quizDto = quizQuestionService.getMistakesQuizByIdWithQuestions(quizId, principal);
        return ResponseEntity.ok(quizDto);
    }
}
