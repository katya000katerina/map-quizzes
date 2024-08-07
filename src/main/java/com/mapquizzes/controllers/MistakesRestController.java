package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.MistakeDto;
import com.mapquizzes.models.dto.PrincipalQuizMistakesDto;
import com.mapquizzes.services.interfaces.MistakeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/mistakes")
@RequiredArgsConstructor
public class MistakesRestController {
    private final MistakeService mistakeService;

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MistakeDto> saveOrUpdate(@Valid @RequestBody MistakeDto mistakeDto,
                                                   Principal principal) {
        return ResponseEntity.accepted()
                .body(mistakeService.saveOrUpdate(mistakeDto, principal));
    }

    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PrincipalQuizMistakesDto>> getMistakesForPrincipal(Principal principal) {
        List<PrincipalQuizMistakesDto> mistakes = mistakeService.getMistakesForPrincipal(principal).collect(Collectors.toList());
        return ResponseEntity.ok(mistakes);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByQuestionIdAndPrincipal(@RequestParam("question-id") Integer questionId,
                                                               Principal principal) {
        mistakeService.deleteByQuestionIdAndPrincipal(questionId, principal);
        return ResponseEntity.noContent().build();
    }

}
