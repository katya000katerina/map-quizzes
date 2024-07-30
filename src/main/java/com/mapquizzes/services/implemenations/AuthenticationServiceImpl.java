package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.custom.EntityNotFoundException;
import com.mapquizzes.exceptions.custom.RefreshTokenException;
import com.mapquizzes.models.dto.AuthenticationDto;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.mapping.mappers.UserMapper;
import com.mapquizzes.repositories.UserRepository;
import com.mapquizzes.services.interfaces.AuthenticationService;
import com.mapquizzes.services.interfaces.JwtService;
import com.mapquizzes.services.interfaces.TokenBlacklistService;
import com.mapquizzes.utils.CookieTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserMapper mapper;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final TokenBlacklistService blacklistService;
    private final AuthenticationManager authManager;
    @Value("${map-quizzes.security.jwt.access-token.expiration-time}")
    private long accessTokenExpTime;
    @Value("${map-quizzes.security.jwt.refresh-token.expiration-time}")
    private long refreshTokenExpTime;

    @Override
    @Transactional
    public AuthenticationDto signUp(UserDto userDto) {
        UserEntity userEntity = mapper.mapDtoToEntity(userDto);
        userEntity.setCreatedAt(OffsetDateTime.now());
        setEncodedPassword(userEntity, userEntity.getPassword());
        userRepo.save(userEntity);

        userDto = mapper.mapEntityToDto(userEntity);
        AuthenticationDto authDto = getAuthenticationDto(userEntity);
        authDto.setUserDto(userDto);
        return authDto;
    }

    @Override
    public void setEncodedPassword(UserEntity userEntity, String passwordToEncode) {
        userEntity.setPassword(encoder.encode(passwordToEncode));
    }

    @Override
    public AuthenticationDto signIn(UserDto userDto) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.username(), userDto.password()
                )
        );
        UserEntity userEntity = userRepo.findByUsername(userDto.username()).orElseThrow();
        return getAuthenticationDto(userEntity);
    }

    @Override
    public AuthenticationDto refreshToken(HttpServletRequest request) {
        String refreshToken = CookieTokenUtils.extractRefreshToken(request);

        if (refreshToken == null || jwtService.isRefreshTokenBlacklisted(refreshToken)) {
            throw new RefreshTokenException();
        }

        String username = jwtService.extractUsername(refreshToken);
        UserEntity userEntity = userRepo.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        UserDetails userDetails = User.builder().username(username).password(userEntity.getPassword()).build();

        if (jwtService.isTokenValid(refreshToken, userDetails)) {
            return getAuthenticationDto(userEntity);
        } else throw new RefreshTokenException();
    }

    @Override
    public AuthenticationDto getNewTokensForPrincipal(UserEntity userEntity, HttpServletRequest request) {
        blacklistService.blacklistAccessAndRefreshTokens(request);
        return getAuthenticationDto(userEntity);
    }

    private AuthenticationDto getAuthenticationDto(UserEntity userEntity) {
        String accessToken = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);
        return new AuthenticationDto(
                makeTokenCookiesHeaders(
                        accessToken, accessTokenExpTime,
                        refreshToken, refreshTokenExpTime
                )
        );
    }

    private HttpHeaders makeTokenCookiesHeaders(String accessToken, long accessTokenExpTime,
                                                String refreshToken, long refreshTokenExpTime) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, makeCookie("accessToken", accessToken, accessTokenExpTime));
        headers.add(HttpHeaders.SET_COOKIE, makeCookie("refreshToken", refreshToken, refreshTokenExpTime));
        return headers;
    }

    private String makeCookie(String name, String token, long expTime) {
        return ResponseCookie.from(name)
                .value(token)
                .maxAge(expTime / 1000)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(Cookie.SameSite.STRICT.toString())
                .build()
                .toString();
    }
}
