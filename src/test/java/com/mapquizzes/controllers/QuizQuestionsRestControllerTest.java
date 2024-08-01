package com.mapquizzes.controllers;

import com.mapquizzes.exceptions.custom.badrequest.InvalidIdException;
import com.mapquizzes.models.dto.QuizDto;
import com.mapquizzes.services.interfaces.QuizQuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class QuizQuestionsRestControllerTest extends BaseControllerTest {
    @MockBean
    private QuizQuestionService service;
    private QuizDto quiz;
    private Integer quizId;
    private Integer invalidId;
    private String errorMessage;

    private void setupOk() {
        quiz = generateQuizDto();
        quizId = quiz.id();
    }

    private QuizDto generateQuizDto() {
        QuizDto.QuestionDto question1 = new QuizDto.QuestionDto(1, "Caspian Sea");
        QuizDto.QuestionDto question2 = new QuizDto.QuestionDto(2, "Victoria");
        QuizDto.QuestionDto question3 = new QuizDto.QuestionDto(3, "Huron");
        return new QuizDto(6, "Lakes of the world",
                new ArrayList<>(
                        Arrays.asList(question1, question2, question3)
                ));
    }

    private void setupBadRequest() {
        invalidId = 100000;
        errorMessage = String.format("Quiz with id=%d doesn't exist", invalidId);
    }

    private void setupUnauthorized() {
        quizId = 8;
    }

    @Test
    void testGetQuizWithQuestions_ReturnsOkAndExpectedJson() throws Exception {
        setupOk();
        when(service.getQuizByIdWithQuestions(quizId)).thenReturn(quiz);
        performGetReturnsOkAndExpectedJson("/api/v1/quizzes-questions/" + quizId);

    }

    @Test
    void testGetQuizWithQuestions_ReturnsBadRequestAndApiErrorJson() throws Exception {
        setupBadRequest();
        when(service.getQuizByIdWithQuestions(invalidId))
                .thenThrow(new InvalidIdException(errorMessage));
        performGetReturnsBadRequestAndApiErrorJson("/api/v1/quizzes-questions/" + invalidId);
    }

    @Test
    @WithMockUser
    void testGetMistakesQuizWithQuestions_ReturnsOkAndExpectedJson() throws Exception {
        setupOk();
        when(service.getMistakesQuizByIdWithQuestions(eq(quizId), any(Principal.class))).thenReturn(quiz);
        performGetReturnsOkAndExpectedJson("/api/v1/quizzes-questions/" + quizId + "/mistakes");
    }

    @Test
    @WithMockUser
    void testGetMistakesQuizWithQuestions_ReturnsBadRequestAndApiErrorJson() throws Exception {
        setupBadRequest();
        when(service.getMistakesQuizByIdWithQuestions(eq(invalidId), any(Principal.class)))
                .thenThrow(new InvalidIdException(errorMessage));
        performGetReturnsBadRequestAndApiErrorJson("/api/v1/quizzes-questions/" + invalidId + "/mistakes");
    }

    @Test
    void testGetMistakesQuizWithQuestions_ReturnsUnauthorized() throws Exception {
        setupUnauthorized();
        mockMvc.perform(get("/api/v1/quizzes-questions/" + quizId + "/mistakes"))
                .andExpect(status().isUnauthorized());
    }

    private void performGetReturnsBadRequestAndApiErrorJson(String path) throws Exception {
        mockMvc.perform(get(path))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus")
                        .value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message")
                        .value(errorMessage));
    }

    private void performGetReturnsOkAndExpectedJson(String path) throws Exception {
        String expectedJson = objectMapper.writeValueAsString(quiz);
        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }
}