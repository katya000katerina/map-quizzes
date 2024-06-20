package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.MistakeDto;

import java.security.Principal;

public interface MistakeService {
    void saveOrUpdate(MistakeDto dto, Principal principal, Integer questionId);
}
