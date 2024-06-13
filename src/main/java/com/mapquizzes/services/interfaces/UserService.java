package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.UserDto;

public interface UserService {
    UserDto getByUsername(String username);
}
