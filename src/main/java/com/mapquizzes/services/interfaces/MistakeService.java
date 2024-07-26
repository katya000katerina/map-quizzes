package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.MistakeDto;
import com.mapquizzes.models.dto.PrincipalQuizMistakesDto;

import java.security.Principal;
import java.util.List;

public interface MistakeService {
    void saveOrUpdate(MistakeDto dto, Principal principal);
    List<PrincipalQuizMistakesDto> getMistakesForPrincipal(Principal principal);

    void deleteByQuestionIdAndPrincipal(Integer questionId, Principal principal);
}
