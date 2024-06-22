package com.mapquizzes.repositories.interfaces;

import com.mapquizzes.models.entities.FastestTimeEntity;
import com.mapquizzes.models.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FastestTimeRepository {
    void saveOrUpdate(FastestTimeEntity entity);


    Optional<FastestTimeEntity> getByUser(UserEntity userEntity);

    Page<FastestTimeEntity> findByQuizIdWithUserSortedByTimeAsc(Integer quizId, Pageable pageable);
}
