package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.MistakeDto;
import com.mapquizzes.models.dto.PrincipalQuizMistakesDto;
import com.mapquizzes.models.entities.UserEntity;

import java.security.Principal;
import java.util.List;

public interface MistakeService {
    MistakeDto saveOrUpdate(MistakeDto dto, Principal principal);
    List<PrincipalQuizMistakesDto> getMistakesForPrincipal(Principal principal);

    void deleteByQuestionIdAndPrincipal(Integer questionId, Principal principal);

    void deleteAllByUser(UserEntity entity);
}
