package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.FastestTimeDto;
import com.mapquizzes.services.interfaces.FastestTimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/fastest-time")
@RequiredArgsConstructor
public class FastestTimeRestController {
    private final FastestTimeService fastestTimeService;

    @PutMapping("/{quizId}")
    public ResponseEntity<Void> saveOrUpdate(@PathVariable Integer quizId,
                                             @Valid @RequestBody FastestTimeDto dto,
                                             Principal principal) {
        fastestTimeService.saveOrUpdate(dto, principal, quizId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
