package com.mapquizzes.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationDto {
    private UserDto userDto;
    private HttpHeaders tokenCookiesHeaders;

    public AuthenticationDto(HttpHeaders tokenCookieHeaders) {
        this.tokenCookiesHeaders = tokenCookieHeaders;
    }
}
