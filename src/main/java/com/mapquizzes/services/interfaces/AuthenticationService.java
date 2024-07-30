package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.AuthenticationDto;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.models.entities.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

public interface AuthenticationService {
    @Transactional
    AuthenticationDto signUp(UserDto userDto);

    void setEncodedPassword(UserEntity userEntity, String passwordToEncode);

    AuthenticationDto signIn(UserDto userDto);

    AuthenticationDto refreshToken(HttpServletRequest request);

    AuthenticationDto getNewTokensForPrincipal(UserEntity userEntity, HttpServletRequest request);
}
