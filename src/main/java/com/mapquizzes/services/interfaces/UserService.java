package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.UserDto;

import java.security.Principal;

public interface UserService {
    UserDto getByUsername(String username);

    Integer getPrincipalId(Principal principal);
}
