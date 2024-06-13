package com.mapquizzes.repositories.interfaces;

import com.mapquizzes.models.entities.UserEntity;

import java.util.Optional;

public interface UserRepository {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> save(UserEntity userEntity);
}
