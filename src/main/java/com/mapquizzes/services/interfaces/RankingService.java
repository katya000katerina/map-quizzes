package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.GlobalRankingDto;
import com.mapquizzes.models.dto.PrincipalRankingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.stream.Stream;

public interface RankingService {
    Page<GlobalRankingDto> getRankingByQuizId(Integer quizId, Pageable pageable);

    Stream<PrincipalRankingDto> getRankingByPrincipal(Principal principal);
}
