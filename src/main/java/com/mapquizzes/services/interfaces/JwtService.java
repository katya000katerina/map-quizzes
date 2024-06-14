package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.entities.UserEntity;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtService {
    boolean isAccessTokenValid(String token, UserDetails user);

    boolean isRefreshTokenValid(String token, UserDetails user);

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> resolver);

    String generateAccessToken(UserEntity user);

    String generateRefreshToken(UserEntity user);
}
