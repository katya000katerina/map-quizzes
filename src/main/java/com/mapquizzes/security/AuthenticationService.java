package com.mapquizzes.security;

import com.mapquizzes.models.dto.AuthenticationDto;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.mapping.mappers.UserMapper;
import com.mapquizzes.repositories.interfaces.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserMapper mapper;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    @Value("${map-quizzes.security.jwt.expiration-time}")
    private long expirationTime;

    @Transactional
    public AuthenticationDto signUp(UserDto userDto) {
        UserEntity entity = mapper.mapDtoToEntity(userDto);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setPassword(encoder.encode(entity.getPassword()));
        userRepo.save(entity);
        userDto = mapper.mapEntityToDto(entity);
        String token = jwtService.generateToken(entity);
        return new AuthenticationDto(userDto, makeCookie(token));
    }

    public AuthenticationDto signIn(UserDto userDto) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.getUsername(), userDto.getPassword()
                )
        );
        UserEntity userEntity = userRepo.findByUsername(userDto.getUsername()).orElseThrow();
        String token = jwtService.generateToken(userEntity);
        return new AuthenticationDto(makeCookie(token));
    }

    private ResponseCookie makeCookie(String token) {
        return ResponseCookie.from("token")
                .value(token)
                .maxAge(expirationTime / 1000)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(Cookie.SameSite.STRICT.toString())
                .build();
    }
}
