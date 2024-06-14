package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.AuthenticationDto;
import com.mapquizzes.models.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

public interface AuthenticationService {
    @Transactional
    AuthenticationDto signUp(UserDto userDto);

    AuthenticationDto signIn(UserDto userDto);

    AuthenticationDto refreshToken(HttpServletRequest request);
}
