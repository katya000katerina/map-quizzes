package com.mapquizzes.repositories;

import com.mapquizzes.models.entities.FastestTimeEntity;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.entities.compositekeys.FastestTimeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FastestTimeRepository extends JpaRepository<FastestTimeEntity, FastestTimeId>,
        PagingAndSortingRepository<FastestTimeEntity, FastestTimeId> {
    List<FastestTimeEntity> findByUser(UserEntity user);

    @Query(value = "select t from FastestTimeEntity t join fetch t.user where t.quiz.id = :quizId",
            countQuery = "select count(t) from FastestTimeEntity t where t.quiz.id = :quizId")
    Page<FastestTimeEntity> findAllByQuizId(Integer quizId, Pageable pageable);
}
