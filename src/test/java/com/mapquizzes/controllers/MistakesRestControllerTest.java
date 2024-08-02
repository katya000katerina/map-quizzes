package com.mapquizzes.controllers;

import com.mapquizzes.exceptions.custom.badrequest.InvalidIdException;
import com.mapquizzes.models.dto.MistakeDto;
import com.mapquizzes.models.dto.PrincipalQuizMistakesDto;
import com.mapquizzes.services.interfaces.MistakeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MistakesRestControllerTest extends BaseControllerTest {
    @MockBean
    private MistakeService mistakeService;

    @Test
    @WithMockUser
    void testSaveOrUpdate_ReturnsAcceptedAndExpectedJson() throws Exception {
        int existingNumberOfMistakes = 4;
        int questionId = 56;
        MistakeDto mockMistakeToSend = new MistakeDto(questionId, 3);
        MistakeDto mockMistakeToReceive =
                new MistakeDto(questionId, mockMistakeToSend.numberOfMistakes() + existingNumberOfMistakes);
        when(mistakeService.saveOrUpdate(eq(mockMistakeToSend), any(Principal.class)))
                .thenReturn(mockMistakeToReceive);

        String json = objectMapper.writeValueAsString(mockMistakeToSend);
        String expectedJson = objectMapper.writeValueAsString(mockMistakeToReceive);
        mockMvc.perform(patch("/api/v1/mistakes")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isAccepted())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser
    void testSaveOrUpdate_ReturnsBadRequestAndApiErrorJson() throws Exception {
        int invalidQuestionId = 56;
        MistakeDto mockMistake = new MistakeDto(invalidQuestionId, 3);
        String errorMessage = String.format("Question with id=%d doesn't exists", invalidQuestionId);
        when(mistakeService.saveOrUpdate(eq(mockMistake), any(Principal.class)))
                .thenThrow(new InvalidIdException(errorMessage));

        String json = objectMapper.writeValueAsString(mockMistake);
        mockMvc.perform(patch("/api/v1/mistakes")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus")
                        .value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message")
                        .value(errorMessage));
    }

    @Test
    void testSaveOrUpdate_ReturnsUnauthorized() throws Exception {
        MistakeDto mockMistake = new MistakeDto(56, 3);

        String json = objectMapper.writeValueAsString(mockMistake);
        mockMvc.perform(patch("/api/v1/mistakes")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testGetMistakesForPrincipal_ReturnsOkAndExpectedJson() throws Exception {
        List<PrincipalQuizMistakesDto> list = TestDataFactory.createListOfPrincipalQuizMistakesDto();
        when(mistakeService.getMistakesForPrincipal(any(Principal.class)))
                .thenReturn(list.stream());

        String expectedJson = objectMapper.writeValueAsString(list);
        mockMvc.perform(get("/api/v1/mistakes/current"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void testGetMistakesForPrincipal_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/mistakes/current"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testDeleteByQuestionIdAndPrincipal_ReturnsNoContent() throws Exception {
        int questionId = 34;
        doNothing().when(mistakeService)
                .deleteByQuestionIdAndPrincipal(eq(questionId), any(Principal.class));

        mockMvc.perform(delete("/api/v1/mistakes")
                        .param("question-id", String.valueOf(questionId)))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteByQuestionIdAndPrincipal_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/mistakes")
                        .param("question-id", String.valueOf(13)))
                .andExpect(status().isUnauthorized());
    }

    private static class TestDataFactory {
        static List<PrincipalQuizMistakesDto> createListOfPrincipalQuizMistakesDto() {
            return Arrays.asList(
                    new PrincipalQuizMistakesDto(1, "Lakes of the world", Arrays.asList(
                            new PrincipalQuizMistakesDto.PrincipalMistakeDto(1, "Hudson", 3),
                            new PrincipalQuizMistakesDto.PrincipalMistakeDto(6, "Caspian Sea", 2),
                            new PrincipalQuizMistakesDto.PrincipalMistakeDto(9, "Tanganyika", 10)
                    )),
                    new PrincipalQuizMistakesDto(2, "Rivers of the world", Arrays.asList(
                            new PrincipalQuizMistakesDto.PrincipalMistakeDto(1, "Nile", 3),
                            new PrincipalQuizMistakesDto.PrincipalMistakeDto(5, "Yangtze", 5),
                            new PrincipalQuizMistakesDto.PrincipalMistakeDto(12, "Yenisei", 1)
                    ))
            );
        }
    }

}