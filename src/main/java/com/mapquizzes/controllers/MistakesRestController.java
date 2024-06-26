package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.MistakeDto;
import com.mapquizzes.services.interfaces.MistakeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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

}
