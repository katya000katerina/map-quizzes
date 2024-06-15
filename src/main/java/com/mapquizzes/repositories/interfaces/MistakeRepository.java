package com.mapquizzes.repositories.interfaces;

import com.mapquizzes.models.entities.MistakeEntity;

import java.util.Optional;

public interface MistakeRepository {
    void saveOrUpdate(MistakeEntity entity);

    Optional<MistakeEntity> getByUserId(Integer userId);
}
