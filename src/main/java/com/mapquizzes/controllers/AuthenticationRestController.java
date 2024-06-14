package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.AuthenticationDto;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.services.interfaces.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
                .headers(authDto.getTokenCookiesHeaders())
                .body(authDto.getUserDto());
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserDto> signIn(@Valid @RequestBody UserDto userDto) {
        AuthenticationDto authDto = authService.signIn(userDto);
        return ResponseEntity
                .ok()
                .headers(authDto.getTokenCookiesHeaders())
                .build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(HttpServletRequest request) {
        AuthenticationDto authDto = authService.refreshToken(request);
        return ResponseEntity
                .ok()
                .headers(authDto.getTokenCookiesHeaders())
                .build();
    }
}
