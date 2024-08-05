package com.mapquizzes.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mapquizzes.models.dto.AuthenticationDto;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.services.interfaces.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.OffsetDateTime;

import static com.mapquizzes.testutils.CookieTestDataFactoryAndUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthenticationRestControllerTest extends BaseControllerTest {
    @MockBean
    private AuthenticationService authService;
    private final static String MOCK_USERNAME = "Charmaine Chelsey";
    private final static String MOCK_PASSWORD = "qwerty";
    private final static String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;

    @Test
    void testSignUp_ReturnsCreatedAndExpectedJsonAndCookies() throws Exception {
        UserDto user = new UserDto(null, MOCK_USERNAME, MOCK_PASSWORD, null);
        AuthenticationDto authDto = createAuthDtoWithUser(new UserDto(1, MOCK_USERNAME, MOCK_PASSWORD, OffsetDateTime.now()));
        when(authService.signUp(user)).thenReturn(authDto);

        String json = TestDataFactory.createJsonWithCredentials();
        String expectedJson = objectMapper.writeValueAsString(authDto.getUserDto());
        mockMvc.perform(post("/api/v1/auth/sign-up")
                        .content(json)
                        .contentType(CONTENT_TYPE))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues(SET_COOKIE_HEADER_NAME, getContainsCookiesInAnyOrder()
                ))
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
        AuthenticationDto authDto = createAuthDtoWithoutUser();
        when(authService.signIn(mockUser)).thenReturn(authDto);

        String json = TestDataFactory.createJsonWithCredentials();
        mockMvc.perform(post("/api/v1/auth/sign-in")
                        .content(json)
                        .contentType(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(header().stringValues(SET_COOKIE_HEADER_NAME, getContainsCookiesInAnyOrder()));

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
        AuthenticationDto authDto = createAuthDtoWithoutUser();
        when(authService.refreshToken(any(String.class)))
                .thenReturn(authDto);

        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .cookie(createRefreshTokenCookie()))
                .andExpect(status().isOk())
                .andExpect(header().stringValues(SET_COOKIE_HEADER_NAME, getContainsCookiesInAnyOrder()));
    }

    @Test
    @WithMockUser
    void testRefreshToken_ReturnsForbidden() throws Exception {
        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .cookie(createRefreshTokenCookie()))
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
    }

}