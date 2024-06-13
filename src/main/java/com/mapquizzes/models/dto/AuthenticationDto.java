package com.mapquizzes.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseCookie;

@AllArgsConstructor
@Getter
public class AuthenticationDto {
    private UserDto userDto;
    private ResponseCookie cookie;

    public AuthenticationDto(ResponseCookie cookie) {
        this.cookie = cookie;
    }
}
