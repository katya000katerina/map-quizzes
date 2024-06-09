package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.QuizDto;

import java.util.stream.Stream;

public interface QuizService {

    Stream<QuizDto> getAll();

    QuizDto getById(Integer id);
}
