package com.mapquizzes.testutils;

import com.mapquizzes.models.dto.AuthenticationDto;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.utils.CookieTokenUtils;
import jakarta.servlet.http.Cookie;
import org.hamcrest.Matcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;

import static com.mapquizzes.testutils.CookieMatcher.cookieMatching;
import static org.hamcrest.Matchers.containsInAnyOrder;

public final class CookieTestDataFactoryAndUtils {
    @Value("${map-quizzes.security.jwt.access-token.expiration-time}")
    private static long accessTokenExpTime;
    @Value("${map-quizzes.security.jwt.refresh-token.expiration-time}")
    private static long refreshTokenExpTime;
    public final static String ACCESS_TOKEN;
    public final static String REFRESH_TOKEN;
    public final static String SET_COOKIE_HEADER_NAME = "Set-Cookie";

    static {
        ACCESS_TOKEN = CookieTokenUtils.makeCookie(
                CookieTokenUtils.Token.ACCESS_TOKEN,
                "mockAccessToken", accessTokenExpTime
        );

        REFRESH_TOKEN = CookieTokenUtils.makeCookie(
                CookieTokenUtils.Token.REFRESH_TOKEN,
                "mockRefreshToken", refreshTokenExpTime
        );
    }

    private CookieTestDataFactoryAndUtils() {
    }

    public static AuthenticationDto createAuthDtoWithoutUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, ACCESS_TOKEN);
        headers.add(HttpHeaders.SET_COOKIE, REFRESH_TOKEN);
        return new AuthenticationDto(headers);
    }

    public static AuthenticationDto createAuthDtoWithUser(UserDto userDto) {
        AuthenticationDto authDto = createAuthDtoWithoutUser();
        authDto.setUserDto(userDto);
        return authDto;
    }

    public static Cookie createAccessTokenCookie() {
        return createCookie("accessToken",
                "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c2VyIn0.IhIoI4uQ88vAtigPmpXbgryezeGYyZt0o01bzpm_Q0-cgJHhLqSsvAIQBGXdxlxG"); // token for a mock user
    }

    public static Cookie createRefreshTokenCookie() {
        return createCookie("refreshToken", "mockRefreshToken");
    }

    public static Matcher<Iterable<? extends String>> getContainsCookiesInAnyOrder() {
        return containsInAnyOrder(
                cookieMatching(ACCESS_TOKEN),
                cookieMatching(REFRESH_TOKEN)
        );
    }

    private static Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}
