package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.MistakeDto;
import com.mapquizzes.models.dto.PrincipalQuizMistakesDto;
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

    @PutMapping
    public ResponseEntity<Void> saveOrUpdate(@Valid @RequestBody MistakeDto dto,
                                             Principal principal) {
        mistakeService.saveOrUpdate(dto, principal);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/current")
    public ResponseEntity<List<PrincipalQuizMistakesDto>> getMistakesForPrincipal(Principal principal) {
        List<PrincipalQuizMistakesDto> mistakes = mistakeService.getMistakesForPrincipal(principal);
        return ResponseEntity.ok(mistakes);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByQuestionIdAndPrincipal(@RequestParam("question-id") Integer questionId,
                                                                      Principal principal) {
        mistakeService.deleteByQuestionIdAndPrincipal(questionId, principal);
        return ResponseEntity.ok().build();
    }

}
