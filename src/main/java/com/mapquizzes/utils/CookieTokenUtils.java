package com.mapquizzes.utils;

import com.mapquizzes.models.dto.TokensDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public final class CookieTokenUtils {
    private CookieTokenUtils() {
    }

    public static HttpHeaders makeTokenCookiesHeaders(String accessToken, long accessTokenExpTime,
                                                String refreshToken, long refreshTokenExpTime) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, makeCookie(Token.ACCESS_TOKEN, accessToken, accessTokenExpTime));
        headers.add(HttpHeaders.SET_COOKIE, makeCookie(Token.REFRESH_TOKEN, refreshToken, refreshTokenExpTime));
        return headers;
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

    public static String makeCookie(Token name, String token, long expTime) {
        return ResponseCookie.from(name.toString())
                .value(token)
                .maxAge(expTime / 1000)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(org.springframework.boot.web.server.Cookie.SameSite.STRICT.toString())
                .build()
                .toString();
    }

    public enum Token {
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

