package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.GlobalRankingDto;
import com.mapquizzes.models.dto.PrincipalRankingDto;
import com.mapquizzes.services.interfaces.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ranking")
@RequiredArgsConstructor
public class RankingRestController {
    private final RankingService rankingService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<GlobalRankingDto>> getByQuizIdWithUsers(@RequestParam("quiz-id") Integer quizId,
                                                                                     Pageable pageable) {
        Page<GlobalRankingDto> globalRankingPage = rankingService.getRankingByQuizId(quizId, pageable);
        return ResponseEntity.ok(globalRankingPage);
    }

    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PrincipalRankingDto>> getByPrincipal(Principal principal) {
        List<PrincipalRankingDto> principalRanking = rankingService.getRankingByPrincipal(principal).collect(Collectors.toList());
        return ResponseEntity.ok(principalRanking);
    }
}
