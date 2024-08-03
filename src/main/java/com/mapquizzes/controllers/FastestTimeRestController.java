package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.TestCompletionTimeDto;
import com.mapquizzes.services.interfaces.FastestTimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/fastest-time")
@RequiredArgsConstructor
public class FastestTimeRestController {
    private final FastestTimeService fastestTimeService;

    @PatchMapping
    public ResponseEntity<TestCompletionTimeDto> saveOrUpdate(@Valid @RequestBody TestCompletionTimeDto complTimeDto,
                                                              Principal principal) {
        return ResponseEntity
                .accepted()
                .body(fastestTimeService.saveOrUpdate(complTimeDto, principal));
    }
}
