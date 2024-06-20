package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.custom.EntityNotFoundException;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.mapping.mappers.UserMapper;
import com.mapquizzes.repositories.interfaces.UserRepository;
import com.mapquizzes.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserMapper mapper;

    @Override
    public UserDto getDtoByPrincipal(Principal principal) {
        return mapper.mapEntityToDto(getEntityByPrincipal(principal));
    }

    @Override
    public UserEntity getEntityByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepo.findByUsername(username).orElseThrow(() -> {
            throw new EntityNotFoundException(String.format("User with username\"%s\" was not found", username));
        });
    }
}
