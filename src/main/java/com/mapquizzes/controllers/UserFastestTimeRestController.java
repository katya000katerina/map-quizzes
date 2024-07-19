package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.FastestTimeDto;
import com.mapquizzes.services.interfaces.FastestTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user-fastest-time")
@RequiredArgsConstructor
public class UserFastestTimeRestController {
    private final FastestTimeService fastestTimeService;

    @GetMapping
    public ResponseEntity<List<FastestTimeDto>> getByPrincipal(Principal principal) {
        return ResponseEntity.ok(fastestTimeService.getDtoByPrincipal(principal).collect(Collectors.toList()));
    }
}
