package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.AuthenticationDto;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.security.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationRestController {
    private final AuthenticationService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody UserDto userDto) {
        AuthenticationDto authDto = authService.signUp(userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, authDto.getCookie().toString())
                .body(authDto.getUserDto());
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserDto> signIn(@Valid @RequestBody UserDto userDto) {
        AuthenticationDto authDto = authService.signIn(userDto);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, authDto.getCookie().toString())
                .build();
    }
}
