package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.TestCompletionTimeDto;
import com.mapquizzes.services.interfaces.FastestTimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/fastest-time")
@RequiredArgsConstructor
public class FastestTimeRestController {
    private final FastestTimeService fastestTimeService;

    @PutMapping
    public ResponseEntity<Void> saveOrUpdate(@Valid @RequestBody TestCompletionTimeDto dto,
                                             Principal principal) {
        fastestTimeService.saveOrUpdate(dto, principal);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
