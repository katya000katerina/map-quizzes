package com.mapquizzes.security;

import com.mapquizzes.services.interfaces.TokenBlacklistService;
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
        String jwtToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length == 0) {
            return;
        }

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("token")) {
                jwtToken = cookie.getValue();
            }
        }

        if (jwtToken == null) {
            return;
        }
        blacklistService.blacklistToken(jwtToken);
    }
}
