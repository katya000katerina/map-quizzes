package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.AuthenticationDto;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.models.entities.UserEntity;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;

public interface UserService {
    UserDto getDtoByPrincipal(Principal principal);
    UserEntity getEntityByPrincipal(Principal principal);

    AuthenticationDto changeUsername(UserDto user, Principal principal, HttpServletRequest request);

    UserDto changePassword(UserDto user, Principal principal);

    void delete(UserEntity user);
}
