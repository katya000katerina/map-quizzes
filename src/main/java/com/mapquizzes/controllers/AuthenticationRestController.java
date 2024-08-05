package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.AuthenticationDto;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.services.interfaces.AuthenticationService;
import com.mapquizzes.validation.groups.user.SignIn;
import com.mapquizzes.validation.groups.user.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationRestController {
    private final AuthenticationService authService;

    @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> signUp(@Validated(SignUp.class) @RequestBody UserDto userDto) {
        AuthenticationDto authDto = authService.signUp(userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(authDto.getTokenCookiesHeaders())
                .body(authDto.getUserDto());
    }

    @PostMapping(value = "/sign-in", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> signIn(@Validated(SignIn.class) @RequestBody UserDto userDto) {
        AuthenticationDto authDto = authService.signIn(userDto);
        return ResponseEntity
                .ok()
                .headers(authDto.getTokenCookiesHeaders())
                .build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        AuthenticationDto authDto = authService.refreshToken(refreshToken);
        return ResponseEntity
                .ok()
                .headers(authDto.getTokenCookiesHeaders())
                .build();
    }
}
