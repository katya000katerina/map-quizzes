package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.custom.NullIdException;
import com.mapquizzes.models.dto.GlobalRankingDto;
import com.mapquizzes.models.dto.PrincipalRankingDto;
import com.mapquizzes.models.mapping.mappers.FastestTimeMapper;
import com.mapquizzes.repositories.FastestTimeRepository;
import com.mapquizzes.services.interfaces.RankingService;
import com.mapquizzes.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {
    private final FastestTimeRepository fastestTimeRepo;
    private final UserService userService;
    private final FastestTimeMapper mapper;

    @Override
    public Page<GlobalRankingDto> getRankingByQuizId(Integer quizId, Pageable pageable) {
        if (quizId == null) {
            throw new NullIdException("Quiz id is null");
        }
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable is null");
        }
        return fastestTimeRepo
                .findAllByQuizId(quizId, pageable)
                .map(mapper::mapEntityToGlobalRankingDto);
    }

    @Override
    public Stream<PrincipalRankingDto> getRankingByPrincipal(Principal principal) {
        List<Object[]> results = fastestTimeRepo.findUserRankings(userService.getEntityByPrincipal(principal).getId());
        return results.stream()
                .map(result -> new PrincipalRankingDto(
                        (String) result[0],
                        ((Number) result[1]).intValue()
                ));
    }
}
