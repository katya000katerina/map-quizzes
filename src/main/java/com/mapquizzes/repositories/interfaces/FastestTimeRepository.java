package com.mapquizzes.repositories.interfaces;

import com.mapquizzes.models.entities.FastestTimeEntity;
import com.mapquizzes.models.entities.UserEntity;

import java.util.Optional;

public interface FastestTimeRepository {
    void saveOrUpdate(FastestTimeEntity entity);


    Optional<FastestTimeEntity> getByUser(UserEntity userEntity);
}
