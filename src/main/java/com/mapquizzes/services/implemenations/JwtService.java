package com.mapquizzes.services.implemenations;

import com.mapquizzes.models.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final TokenBlacklistService blacklistService;
    @Value("${map-quizzes.security.jwt.secret-key}")
    private String secretKey;
    @Value("${map-quizzes.security.jwt.access-token.expiration-time}")
    private long accessTokenExpTime;
    @Value("${map-quizzes.security.jwt.refresh-token.expiration-time}")
    private long refreshTokenExpTime;

    public boolean isAccessTokenValid(String token, UserDetails user) {
        return isTokenValid(token, user) && !isAccessTokenBlacklisted(token);
    }

    public boolean isRefreshTokenValid(String token, UserDetails user) {
        return isTokenValid(token, user) && !isRefreshTokenBlacklisted(token);
    }

    private boolean isTokenValid(String token, UserDetails user){
        String username = extractUsername(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    private boolean isAccessTokenBlacklisted(String token) {
        return blacklistService.getBlacklistedAccessToken(token) != null;
    }

    private boolean isRefreshTokenBlacklisted(String token) {
        return blacklistService.getBlacklistedRefreshToken(token) != null;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateAccessToken(UserEntity user) {
        return generateToken(user, accessTokenExpTime);
    }

    public String generateRefreshToken(UserEntity user) {
        return generateToken(user, refreshTokenExpTime);
    }

    private String generateToken(UserEntity user, long expTime){
        return Jwts
                .builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expTime))
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
