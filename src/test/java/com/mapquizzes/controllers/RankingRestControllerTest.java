package com.mapquizzes.controllers;

import com.jayway.jsonpath.JsonPath;
import com.mapquizzes.exceptions.custom.badrequest.InvalidIdException;
import com.mapquizzes.models.dto.GlobalRankingDto;
import com.mapquizzes.models.dto.PrincipalRankingDto;
import com.mapquizzes.services.interfaces.RankingService;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.security.Principal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RankingRestControllerTest extends BaseControllerTest {
    @MockBean
    private RankingService rankingService;

    @Test
    void testGetByQuizIdWithUsers_ReturnsOkAndExpectedJson() throws Exception {
        Integer quizId = 4;
        int pageNumber = 0;
        int size = 10;
        Sort.Direction direction = Sort.Direction.ASC;
        String field = "timeInMillis";
        Page<GlobalRankingDto> mockPage = TestDataFactory.createPageOfGlobalRankingDto(pageNumber, size, direction, field);
        when(rankingService.getRankingByQuizId(eq(quizId), any(Pageable.class))).thenReturn(mockPage);

        MvcResult result =
                performGetGlobalRanking(quizId, pageNumber, size, field, direction)
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content", hasSize(mockPage.getSize())))
                        .andExpect(jsonPath("$.page.totalElements", is((int) mockPage.getTotalElements())))
                        .andExpect(jsonPath("$.page.totalPages", is(mockPage.getTotalPages())))
                        .andExpect(jsonPath("$.page.number", is(mockPage.getNumber())))
                        .andReturn();

        String fullJson = result.getResponse().getContentAsString();
        String actualInnerArray = JsonPath.read(fullJson, "$.content").toString();
        String expectedInnerArray = objectMapper.writeValueAsString(mockPage.getContent());
        JSONAssert.assertEquals(expectedInnerArray, actualInnerArray, JSONCompareMode.STRICT_ORDER);
    }

    @Test
    void testGetByQuizIdWithUsers_ReturnsBadRequestAndApiErrorJson() throws Exception {
        Integer invalidQuizId = 10000000;
        String errorMessage = String.format("Quiz with id=%d doesn't exist", invalidQuizId);
        when(rankingService.getRankingByQuizId(eq(invalidQuizId), any(Pageable.class)))
                .thenThrow(new InvalidIdException(errorMessage));

        performGetGlobalRanking(invalidQuizId, 2, 10, "timeInMillis", Sort.Direction.ASC)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus")
                        .value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message")
                        .value(errorMessage));
    }

    @Test
    @WithMockUser
    void testGetByPrincipal_ReturnsOkAndExpectedJson() throws Exception {
        List<PrincipalRankingDto> list = TestDataFactory.createListOfPrincipalRankingDto();
        when(rankingService.getRankingByPrincipal(any(Principal.class))).thenReturn(list.stream());

        String expectedJson = objectMapper.writeValueAsString(list);
        mockMvc.perform(get("/api/v1/ranking/current"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void testGetByPrincipal_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/ranking/current"))
                .andExpect(status().isUnauthorized());
    }

    private ResultActions performGetGlobalRanking(int quizId, int pageNumber, int size, String field, Sort.Direction direction) throws Exception {
        return mockMvc.perform(get("/api/v1/ranking")
                .param("quiz-id", String.valueOf(quizId))
                .param("page", String.valueOf(pageNumber))
                .param("size", String.valueOf(size))
                .param("sort", String.format("%s,%s", field, direction.toString().toLowerCase())));
    }

    private static class TestDataFactory {
        static Page<GlobalRankingDto> createPageOfGlobalRankingDto(int pageNumber, int size,
                                                                   Sort.Direction direction, String field) {

            List<GlobalRankingDto> list = Arrays.asList(
                    new GlobalRankingDto("Ranulph Franklyn", 456784),
                    new GlobalRankingDto("Morty London", 234656),
                    new GlobalRankingDto("Aline Sommer", 76234),
                    new GlobalRankingDto("Joss Beckett", 345768),
                    new GlobalRankingDto("Anson Liv", 65311),
                    new GlobalRankingDto("Lonnie Roosevelt", 564731),
                    new GlobalRankingDto("Terry Anya", 34667),
                    new GlobalRankingDto("Lena Leighton", 65456),
                    new GlobalRankingDto("Janna Ruby", 42556),
                    new GlobalRankingDto("Tod Jeane", 223456)
            );
            list.sort(Comparator.comparing(GlobalRankingDto::timeInMillis));

            Sort sort = Sort.by(direction, field);
            int total = list.size();
            Pageable pageable = PageRequest.of(pageNumber, size, sort);
            return new PageImpl<>(list, pageable, total);
        }

        static List<PrincipalRankingDto> createListOfPrincipalRankingDto() {
            return Arrays.asList(
                    new PrincipalRankingDto("Lakes of the world", 5),
                    new PrincipalRankingDto("Rivers of the world", 15),
                    new PrincipalRankingDto("Deserts of the world", 7)
            );
        }
    }

}