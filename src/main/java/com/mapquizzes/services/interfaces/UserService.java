package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.models.entities.UserEntity;

import java.security.Principal;

public interface UserService {
    UserDto getDtoByPrincipal(Principal principal);
    UserEntity getEntityByPrincipal(Principal principal);
}
