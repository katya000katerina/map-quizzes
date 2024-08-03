package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.MistakeDto;
import com.mapquizzes.models.dto.PrincipalQuizMistakesDto;
import com.mapquizzes.models.entities.UserEntity;

import java.security.Principal;
import java.util.stream.Stream;

public interface MistakeService {
    MistakeDto saveOrUpdate(MistakeDto mistakeDto, Principal principal);

    Stream<PrincipalQuizMistakesDto> getMistakesForPrincipal(Principal principal);

    void deleteByQuestionIdAndPrincipal(Integer questionId, Principal principal);

    void deleteAllByUser(UserEntity userEntity);
}
