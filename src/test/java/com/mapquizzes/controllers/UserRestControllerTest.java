package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.AuthenticationDto;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.services.interfaces.UserDeletionService;
import com.mapquizzes.services.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;
import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserRestControllerTest extends BaseControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private UserDeletionService userDeletionService;
    private UserDto user;
    private final String username = "Kipling Reene";
    private final String password = "qwerty";

    private void setupUser() {
        user = new UserDto(56, username, password, OffsetDateTime.now());
    }


    @Test
    @WithMockUser
    void testGetSignedInUser_ReturnsOkAndExpectedJson() throws Exception {
        setupUser();
        when(userService.getDtoByPrincipal(any(Principal.class))).thenReturn(user);

        String expectedJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(get("/api/v1/users/current"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void testGetSignedInUser_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/users/current"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testChangeUsername_ReturnsAcceptedAndExpectedJson() throws Exception {
        setupUser();
        UserDto changeUsernameUser = new UserDto(null, username, null, null);
        AuthenticationDto authDto = new AuthenticationDto(user, new HttpHeaders());
        when(userService.changeUsername(eq(changeUsernameUser), any(Principal.class), any(HttpServletRequest.class)))
                .thenReturn(authDto);
        performPatch_ReturnsAcceptedAndExpectedJson("/api/v1/users/username",
                "{\"username\": \"" + username + "\"}");
    }

    @ParameterizedTest
    @CsvSource({"Karim Chasity, username:The username is not available",
            "'', username:Username cannot be blank"})
    @WithMockUser
    void testChangeUsername_ReturnsBadRequestAndApiErrorJson(String invalidUsername, String errorMessage) throws Exception {
        performPatch_ReturnsBadRequestAndApiErrorJson("/api/v1/users/username",
                "{\"username\": \"" + invalidUsername + "\"}", errorMessage);
    }

    @Test
    void testChangeUsername_ReturnsUnauthorized() throws Exception {
        performPatch_ReturnsUnauthorized("/api/v1/users/username",
                "{\"username\": \"" + username + "\"}");
    }

    @Test
    @WithMockUser
    void testChangePassword_ReturnsAcceptedAndExpectedJson() throws Exception {
        setupUser();
        UserDto changePasswordUser = new UserDto(null, null, password, null);
        when(userService.changePassword(eq(changePasswordUser), any(Principal.class)))
                .thenReturn(user);

        performPatch_ReturnsAcceptedAndExpectedJson("/api/v1/users/password",
                "{\"password\": \"" + password + "\"}");

    }

    @Test
    @WithMockUser
    void testChangePassword_ReturnsBadRequestAndApiErrorJson() throws Exception {
        performPatch_ReturnsBadRequestAndApiErrorJson("/api/v1/users/password",
                "{\"password\": \"\"}", "password:Password cannot be blank");
    }

    @Test
    void testChangePassword_ReturnsUnauthorized() throws Exception {
        performPatch_ReturnsUnauthorized("/api/v1/users/password",
                "{\"password\": \"" + password + "\"}");
    }

    @Test
    @WithMockUser
    void testDeleteSignedInUser_ReturnsNoContent() throws Exception {
        doNothing().when(userDeletionService)
                .deletePrincipal(any(Principal.class), any(HttpServletRequest.class));

        mockMvc.perform(delete("/api/v1/users/current"))
                .andExpect(status().isNoContent());
    }

    @Test
       void testDeleteSignedInUser_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/users/current"))
                .andExpect(status().isUnauthorized());
    }

    private void performPatch_ReturnsUnauthorized(String path, String json) throws Exception {
        mockMvc.perform(patch(path)
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isUnauthorized());
    }

    private void performPatch_ReturnsBadRequestAndApiErrorJson(String path,
                                                               String json,
                                                               String errorMessage) throws Exception {
        mockMvc.perform(patch(path)
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus")
                        .value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message")
                        .value("Validation error"))
                .andExpect(jsonPath("$.errors[0]")
                        .value(errorMessage));
    }

    private void performPatch_ReturnsAcceptedAndExpectedJson(String path, String json) throws Exception {
        String expectedJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(patch(path)
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedJson));
    }
}