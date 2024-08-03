package com.mapquizzes.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mapquizzes.models.dto.AuthenticationDto;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.services.interfaces.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.OffsetDateTime;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthenticationRestControllerTest extends BaseControllerTest {
    @MockBean
    private AuthenticationService authService;
    private final static String MOCK_USERNAME = "Charmaine Chelsey";
    private final static String MOCK_PASSWORD = "qwerty";
    private final static String ACCESS_TOKEN = "accessToken=mockAccessToken";
    private final static String REFRESH_TOKEN = "accessToken=mockAccessToken";
    private final static String HEADER_NAME = "Set-Cookie";
    private final static String CONTENT_TYPE = "application/json";

    @Test
    void testSignUp_ReturnsCreatedAndExpectedJsonAndCookies() throws Exception {
        UserDto user = new UserDto(null, MOCK_USERNAME, MOCK_PASSWORD, null);
        AuthenticationDto authDto = TestDataFactory.createAuthDtoWithUser();
        when(authService.signUp(user)).thenReturn(authDto);

        String json = TestDataFactory.createJsonWithCredentials();
        String expectedJson = objectMapper.writeValueAsString(authDto.getUserDto());
        mockMvc.perform(post("/api/v1/auth/sign-up")
                        .content(json)
                        .contentType(CONTENT_TYPE))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues(HEADER_NAME, containsInAnyOrder(
                        ACCESS_TOKEN,
                        REFRESH_TOKEN
                )))
                .andExpect(content().json(expectedJson));

    }

    @Test
    @WithMockUser
    void testSignUp_ReturnsForbidden() throws Exception {
        String json = TestDataFactory.createJsonWithCredentials();
        mockMvc.perform(post("/api/v1/auth/sign-up")
                        .content(json)
                        .contentType(CONTENT_TYPE))
                .andExpect(status().isForbidden());
    }

    @Test
    void testSignIn_ReturnsOkAndCookies() throws Exception {
        UserDto mockUser = new UserDto(null, MOCK_USERNAME, MOCK_PASSWORD, null);
        AuthenticationDto authDto = TestDataFactory.createAuthDtoWithoutUser();
        when(authService.signIn(mockUser)).thenReturn(authDto);

        String json = TestDataFactory.createJsonWithCredentials();
        mockMvc.perform(post("/api/v1/auth/sign-in")
                        .content(json)
                        .contentType(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().stringValues(HEADER_NAME, containsInAnyOrder(
                        ACCESS_TOKEN,
                        REFRESH_TOKEN
                )));

    }

    @Test
    @WithMockUser
    void testSignIn_ReturnsForbidden() throws Exception {
        String json = TestDataFactory.createJsonWithCredentials();
        mockMvc.perform(post("/api/v1/auth/sign-in")
                        .content(json)
                        .contentType(CONTENT_TYPE))
                .andExpect(status().isForbidden());
    }

    @Test
    void testRefreshToken_ReturnsOkAndCookies() throws Exception {
        AuthenticationDto authDto = TestDataFactory.createAuthDtoWithoutUser();
        when(authService.refreshToken(any(HttpServletRequest.class)))
                .thenReturn(authDto);

        mockMvc.perform(post("/api/v1/auth/refresh-token"))
                .andExpect(status().isOk())
                .andExpect(header().stringValues(HEADER_NAME, containsInAnyOrder(
                        ACCESS_TOKEN,
                        REFRESH_TOKEN
                )));
    }

    @Test
    @WithMockUser
    void testRefreshToken_ReturnsForbidden() throws Exception {
        mockMvc.perform(post("/api/v1/auth/refresh-token"))
                .andExpect(status().isForbidden());
    }

    private static class TestDataFactory {
        static String createJsonWithCredentials() throws JsonProcessingException {
            ObjectMapper innerObjectMapper = new ObjectMapper();
            ObjectNode jsonNode = innerObjectMapper.createObjectNode();
            jsonNode.put("username", MOCK_USERNAME);
            jsonNode.put("password", MOCK_PASSWORD);
            return innerObjectMapper.writeValueAsString(jsonNode);
        }

        static AuthenticationDto createAuthDtoWithoutUser() {
            String[] access = ACCESS_TOKEN.split("=");
            String[] refresh = REFRESH_TOKEN.split("=");

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE,
                    ResponseCookie
                            .from(access[0])
                            .value(access[1])
                            .build()
                            .toString());
            headers.add(HttpHeaders.SET_COOKIE,
                    ResponseCookie
                            .from(refresh[0])
                            .value(refresh[1])
                            .build()
                            .toString());
            return new AuthenticationDto(headers);
        }

        static AuthenticationDto createAuthDtoWithUser() {
            AuthenticationDto authDto = createAuthDtoWithoutUser();
            authDto.setUserDto(new UserDto(1, MOCK_USERNAME, MOCK_PASSWORD, OffsetDateTime.now()));
            return authDto;
        }
    }

}