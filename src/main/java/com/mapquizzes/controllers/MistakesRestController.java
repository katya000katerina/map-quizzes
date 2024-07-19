package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.MistakeDto;
import com.mapquizzes.models.dto.PrincipalQuizMistakes;
import com.mapquizzes.services.interfaces.MistakeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mistakes")
@RequiredArgsConstructor
public class MistakesRestController {
    private final MistakeService mistakeService;

    @PutMapping("/{questionId}")
    public ResponseEntity<Void> saveOrUpdate(@PathVariable Integer questionId,
                                             @Valid @RequestBody MistakeDto dto,
                                             Principal principal) {
        mistakeService.saveOrUpdate(dto, principal, questionId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/current")
    public ResponseEntity<List<PrincipalQuizMistakes>> getMistakesForPrincipal(Principal principal) {
        List<PrincipalQuizMistakes> mistakes = mistakeService.getMistakesForPrincipal(principal);
        return ResponseEntity.ok(mistakes);
    }

}
