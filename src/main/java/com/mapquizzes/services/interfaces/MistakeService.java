package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.MistakeDto;
import com.mapquizzes.models.dto.PrincipalQuizMistakes;

import java.security.Principal;
import java.util.List;

public interface MistakeService {
    void saveOrUpdate(MistakeDto dto, Principal principal, Integer questionId);
    List<PrincipalQuizMistakes> getMistakesForPrincipal(Principal principal);

}
