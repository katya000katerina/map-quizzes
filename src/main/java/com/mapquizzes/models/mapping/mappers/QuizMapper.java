package com.mapquizzes.models.mapping.mappers;

import com.mapquizzes.models.dto.QuizDto;
import com.mapquizzes.models.entities.QuizEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface QuizMapper {

    @Named("withQuestions")
    @Mapping(target = "questions[].quiz", qualifiedByName = "withQuestions")
    QuizDto mapEntityToDto(QuizEntity entity);

    @Mapping(target = "questions", ignore = true)
    QuizDto mapEntityToDtoWithoutQuestions(QuizEntity entity);
}
