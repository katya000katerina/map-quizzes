package com.mapquizzes.repositories.interfaces;

import com.mapquizzes.models.entities.QuizEntity;

import java.util.Optional;
import java.util.stream.Stream;

public interface QuizRepository {

    Stream<QuizEntity> findAll();

    Optional<QuizEntity> findById(Integer id);
}
