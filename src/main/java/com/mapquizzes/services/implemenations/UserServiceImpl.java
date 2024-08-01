package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.custom.internalservererror.EntityNotFoundException;
import com.mapquizzes.models.dto.AuthenticationDto;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.mapping.mappers.UserMapper;
import com.mapquizzes.repositories.UserRepository;
import com.mapquizzes.services.interfaces.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserMapper mapper;

    private final AuthenticationService authService;

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

    @Transactional
    @Override
    public AuthenticationDto changeUsername(UserDto dto, Principal principal, HttpServletRequest request) {
        UserEntity entity = getEntityByPrincipal(principal);
        entity.setUsername(dto.username());
        entity = userRepo.save(entity);
        AuthenticationDto authDto = authService.getNewTokensForPrincipal(entity, request);
        authDto.setUserDto(mapper.mapEntityToDto(entity));
        return authDto;
    }

    @Transactional
    @Override
    public UserDto changePassword(UserDto dto, Principal principal) {
        UserEntity entity = getEntityByPrincipal(principal);
        authService.setEncodedPassword(entity, dto.password());
        entity = userRepo.save(entity);
        return mapper.mapEntityToDto(entity);
    }

    @Transactional
    @Override
    public void delete(UserEntity user) {
        userRepo.delete(user);
    }
}
