package com.mapquizzes.repositories;

import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.entities.UserImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImageEntity, Integer> {

    Optional<UserImageEntity> findByUser(UserEntity user);

    @Query("select i.id from UserImageEntity i where i.user = :user")
    Optional<Integer> getIdByUser(UserEntity user);
}
