package com.mapquizzes.controllers;


import com.fasterxml.jackson.databind.ObjectWriter;
import com.mapquizzes.exceptions.custom.badrequest.InvalidIdException;
import com.mapquizzes.models.dto.QuizDto;
import com.mapquizzes.models.dto.views.QuizViews;
import com.mapquizzes.services.interfaces.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class QuizRestControllerTest extends BaseControllerTest {
    @MockBean
    private QuizService quizService;
    private ObjectWriter objectWriter;

    @BeforeEach
    void setup() {
        objectWriter = objectMapper
                .writerWithView(QuizViews.WithoutQuestions.class);
    }

    @Test
    void testGetAll_ReturnsOkAndExpectedJson() throws Exception {
        List<QuizDto> list = Arrays.asList(
                new QuizDto(1, "Lakes of the world", new ArrayList<>()),
                new QuizDto(2, "Rivers of the world", new ArrayList<>()),
                new QuizDto(3, "Deserts of the world", new ArrayList<>())
        );
        when(quizService.getAllDto()).thenReturn(list.stream());

        String expectedJson = objectWriter.writeValueAsString(list);

        mockMvc.perform(get("/api/v1/quizzes"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void testGetById_ReturnsOkAndExpectedJson() throws Exception {
        Integer quizId = 4;
        QuizDto quiz = new QuizDto(quizId, "Seas of the world", new ArrayList<>());
        when(quizService.getDtoById(quizId)).thenReturn(quiz);

        String expectedJson = objectWriter.writeValueAsString(quiz);
        mockMvc.perform(get("/api/v1/quizzes/" + quizId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void testGetById_ReturnsBadRequestAndApiErrorJson() throws Exception {
        Integer invalidQuizId = 1000000;
        String errorMessage = String.format("Quiz with id=%d doesn't exist", invalidQuizId);
        when(quizService.getDtoById(invalidQuizId))
                .thenThrow(new InvalidIdException(errorMessage));

        mockMvc.perform(get("/api/v1/quizzes/" + invalidQuizId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus")
                        .value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message")
                        .value(errorMessage));
    }
}