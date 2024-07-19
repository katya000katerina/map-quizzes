package com.mapquizzes.repositories;

import com.mapquizzes.models.entities.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<QuizEntity, Integer> {
}
