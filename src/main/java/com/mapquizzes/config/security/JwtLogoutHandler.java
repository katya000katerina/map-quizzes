package com.mapquizzes.config.security;

import com.mapquizzes.services.implemenations.TokenBlacklistService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {
    private final TokenBlacklistService blacklistService;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String accessToken = null;
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length == 0) {
            return;
        }

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("access_token")) {
                accessToken = cookie.getValue();
            }
            if (cookie.getName().equals("refresh_token")) {
                refreshToken = cookie.getValue();
            }
        }

        blacklistService.blacklistAccessToken(accessToken);
        blacklistService.blacklistRefreshToken(refreshToken);
    }
}
