package com.mapquizzes.models.mapping.mappers;

import com.mapquizzes.models.dto.QuizDto;
import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.models.mapping.CycleAvoidingContext;
import com.mapquizzes.models.mapping.MapstructIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface QuizMapper {

    @MapstructIgnore
    default QuizDto mapEntityToDto(QuizEntity entity) {
        return mapEntityToDto(entity, new CycleAvoidingContext());
    }

    @Named("main")
    @Mapping(target = "questions[].quiz", qualifiedByName = "main")
    QuizDto mapEntityToDto(QuizEntity entity, @Context CycleAvoidingContext context);

    @Mapping(target = "questions", ignore = true)
    QuizDto mapEntityToDtoWithoutQuestions(QuizEntity entity);
}
