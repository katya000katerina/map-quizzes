package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.QuizCompletionFastestTimeDto;
import com.mapquizzes.services.interfaces.QuizCompletionFastestTimeService;
import com.mapquizzes.services.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/fastest-time")
@RequiredArgsConstructor
public class QuizCompletionFastestTimeRestController {
    private final QuizCompletionFastestTimeService fastestTimeService;
    private final UserService userService;

    @PutMapping("/{quizId}")
    public ResponseEntity<Void> saveOrUpdate(@PathVariable Integer quizId,
                                             @Valid @RequestBody QuizCompletionFastestTimeDto dto,
                                             Principal principal) {
        Integer userId = userService.getPrincipalId(principal);
        fastestTimeService.saveOrUpdate(dto, userId, quizId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
