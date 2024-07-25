package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.AuthenticationDto;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.services.interfaces.AuthenticationService;
import com.mapquizzes.validation.groups.user.SignIn;
import com.mapquizzes.validation.groups.user.SignUp;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<UserDto> signUp(@Validated(SignUp.class) @RequestBody UserDto userDto) {
        AuthenticationDto authDto = authService.signUp(userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(authDto.getTokenCookiesHeaders())
                .body(authDto.getUserDto());
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserDto> signIn(@Validated(SignIn.class) @RequestBody UserDto userDto) {
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
