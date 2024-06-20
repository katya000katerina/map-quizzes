package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService service;

    @GetMapping("/current")
    public ResponseEntity<UserDto> getSignedInUser(Principal principal) {
        return ResponseEntity.ok(service.getDtoByPrincipal(principal));
    }
}
