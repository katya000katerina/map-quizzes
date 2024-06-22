package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.FastestTimeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

public interface FastestTimeService {
    void saveOrUpdate(FastestTimeDto dto, Principal principal, Integer quizId);

    Page<FastestTimeDto> getDtoByQuizIdWithUserSortedByTimeAsc(Integer quizId, Pageable pageable);
}
