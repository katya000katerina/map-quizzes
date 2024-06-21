package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.FastestTimeDto;

import java.security.Principal;

public interface FastestTimeService {
    void saveOrUpdate(FastestTimeDto dto, Principal principal, Integer quizId);
}
