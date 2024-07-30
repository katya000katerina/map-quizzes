package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.TestCompletionTimeDto;
import com.mapquizzes.models.entities.UserEntity;

import java.security.Principal;

public interface FastestTimeService {
    void saveOrUpdate(TestCompletionTimeDto dto, Principal principal);

    void deleteAllByUser(UserEntity entity);
}
