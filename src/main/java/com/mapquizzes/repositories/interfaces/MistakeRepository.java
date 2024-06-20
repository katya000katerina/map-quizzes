package com.mapquizzes.repositories.interfaces;

import com.mapquizzes.models.entities.MistakeEntity;
import com.mapquizzes.models.entities.compositekeys.MistakeId;

import java.util.Optional;

public interface MistakeRepository {
    void saveOrUpdate(MistakeEntity entity);

    Optional<MistakeEntity> findById(MistakeId mistakeId);
}
