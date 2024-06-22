package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.QuizDto;
import com.mapquizzes.models.entities.QuizEntity;

import java.util.stream.Stream;

public interface QuizService {

    Stream<QuizDto> getAllDto();

    QuizEntity getEntityById(Integer id);

    QuizDto getDtoById(Integer id);
}
