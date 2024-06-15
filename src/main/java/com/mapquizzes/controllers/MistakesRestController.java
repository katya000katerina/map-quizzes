package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.MistakeDto;
import com.mapquizzes.services.interfaces.MistakeService;
import com.mapquizzes.services.interfaces.UserService;
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
    private final UserService userService;

    @PutMapping("/{questionId}")
    public ResponseEntity<Void> saveOrUpdate(@PathVariable Integer questionId,
                                             @Valid @RequestBody MistakeDto dto,
                                             Principal principal) {
        Integer userId = userService.getPrincipalId(principal);
        dto.setUserId(userId);
        dto.setQuestionId(questionId);
        mistakeService.saveOrUpdate(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
