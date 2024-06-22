package com.mapquizzes.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.mapquizzes.models.dto.QuizDto;
import com.mapquizzes.models.dto.views.QuizViews;
import com.mapquizzes.services.interfaces.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizRestController {

    private final QuizService service;

    @JsonView(QuizViews.WithoutQuestions.class)
    @GetMapping
    public ResponseEntity<List<QuizDto>> getAll() {
        return ResponseEntity.ok(service.getAllDto().collect(Collectors.toList()));
    }

    @JsonView(QuizViews.WithoutQuestions.class)
    @GetMapping("/{id}")
    public ResponseEntity<QuizDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getDtoById(id));
    }
}
