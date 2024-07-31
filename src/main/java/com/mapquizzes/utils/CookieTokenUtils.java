package com.mapquizzes.utils;

import com.mapquizzes.models.dto.TokensDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public final class CookieTokenUtils {
    private CookieTokenUtils() {
    }

    public static String extractRefreshToken(HttpServletRequest request) {
        return extractTokens(getCookies(request)).refreshToken();
    }

    public static String extractAccessToken(HttpServletRequest request) {
        return extractTokens(getCookies(request)).accessToken();
    }

    public static TokensDto extractAccessAndRefreshTokens(HttpServletRequest request) {
        return extractTokens(getCookies(request));
    }

    private static Cookie[] getCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length == 0) {
            return new Cookie[]{};
        }

        return cookies;
    }

    private static TokensDto extractTokens(Cookie[] cookies) {
        String accessToken = null;
        String refreshToken = null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(Token.ACCESS_TOKEN.toString())) {
                accessToken = cookie.getValue();
            } else if (cookie.getName().equals(Token.REFRESH_TOKEN.toString())) {
                refreshToken = cookie.getValue();
            }

            if (accessToken != null && refreshToken != null) {
                break;
            }
        }

        return new TokensDto(accessToken, refreshToken);
    }

    private enum Token {
        ACCESS_TOKEN {
            @Override
            public String toString() {
                return "accessToken";
            }
        },
        REFRESH_TOKEN {
            @Override
            public String toString() {
                return "refreshToken";
            }
        }
    }
}

