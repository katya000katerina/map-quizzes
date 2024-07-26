package com.mapquizzes.repositories;

import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<QuizEntity, Integer> {

    @Query("select qz from QuizEntity qz join fetch qz.questions qs " +
            "join MistakeEntity m on m.question.id = qs.id " +
            "where m.user = :user and qz.id = :quizId")
    Optional<QuizEntity> findMistakesQuizByIdAndUser(Integer quizId, UserEntity user);
}
