package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.custom.internalservererror.EntityNotFoundException;
import com.mapquizzes.models.dto.AuthenticationDto;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.mapping.mappers.UserMapper;
import com.mapquizzes.repositories.UserRepository;
import com.mapquizzes.services.interfaces.AuthenticationService;
import com.mapquizzes.services.interfaces.TokenBlacklistService;
import com.mapquizzes.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;

    private final AuthenticationService authService;
    private final TokenBlacklistService blacklistService;

    @Override
    public UserDto getDtoByPrincipal(Principal principal) {
        return userMapper.mapEntityToDto(getEntityByPrincipal(principal));
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
    public AuthenticationDto changeUsername(UserDto userDto, Principal principal, String accessToken, String refreshToken) {
        UserEntity userEntity = getEntityByPrincipal(principal);
        userEntity.setUsername(userDto.username());
        userEntity = userRepo.save(userEntity);

        blacklistService.blacklistAccessAndRefreshTokens(accessToken, refreshToken);
        AuthenticationDto authDto = authService.getAuthenticationDto(userEntity);
        authDto.setUserDto(userMapper.mapEntityToDto(userEntity));
        return authDto;
    }

    @Transactional
    @Override
    public UserDto changePassword(UserDto userDto, Principal principal) {
        UserEntity userEntity = getEntityByPrincipal(principal);
        authService.setEncodedPassword(userEntity, userDto.password());
        userEntity = userRepo.save(userEntity);
        return userMapper.mapEntityToDto(userEntity);
    }

    @Transactional
    @Override
    public void delete(UserEntity userEntity) {
        userRepo.delete(userEntity);
    }
}
