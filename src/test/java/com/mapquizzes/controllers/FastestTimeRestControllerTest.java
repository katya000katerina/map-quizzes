package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.TestCompletionTimeDto;
import com.mapquizzes.services.interfaces.FastestTimeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FastestTimeRestControllerTest extends BaseControllerTest {
    @MockBean
    private FastestTimeService fastestTimeService;

    @Test
    @WithMockUser
    void testSaveOrUpdate_ReturnsAcceptedAndExpectedJson() throws Exception {
        int quizId = 4;
        int timeInMillis = 20500;
        TestCompletionTimeDto mockTimeToSend = new TestCompletionTimeDto(quizId, timeInMillis);
        TestCompletionTimeDto mockTimeToReceive = new TestCompletionTimeDto(quizId, timeInMillis - 1000);
        when(fastestTimeService.saveOrUpdate(eq(mockTimeToSend), any(Principal.class)))
                .thenReturn(mockTimeToReceive);

        String json = objectMapper.writeValueAsString(mockTimeToSend);
        String expectedJson = objectMapper.writeValueAsString(mockTimeToReceive);
        mockMvc.perform(patch("/api/v1/fastest-time")
                .content(json)
                .contentType("application/json"))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void testSaveOrUpdate_ReturnsUnauthorized() throws Exception {
        TestCompletionTimeDto mockTimeToSend = new TestCompletionTimeDto(5, 235676);

        String json = objectMapper.writeValueAsString(mockTimeToSend);
        mockMvc.perform(patch("/api/v1/fastest-time")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isUnauthorized());
    }
}