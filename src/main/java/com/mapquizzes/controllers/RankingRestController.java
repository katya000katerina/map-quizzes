package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.GlobalRankingDto;
import com.mapquizzes.models.dto.PrincipalRankingDto;
import com.mapquizzes.services.interfaces.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping
    public ResponseEntity<Page<GlobalRankingDto>> getByQuizIdWithUser(@RequestParam("quiz-id") Integer quizId,
                                                                                     Pageable pageable) {
        return ResponseEntity.ok(rankingService.getRankingByQuizId(quizId, pageable));
    }

    @GetMapping("/current")
    public ResponseEntity<List<PrincipalRankingDto>> getByPrincipal(Principal principal) {
        return ResponseEntity.ok(rankingService.getRankingByPrincipal(principal).collect(Collectors.toList()));
    }
}
