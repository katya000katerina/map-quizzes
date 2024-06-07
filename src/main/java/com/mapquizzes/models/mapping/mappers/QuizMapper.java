package com.mapquizzes.models.mapping.mappers;

import com.mapquizzes.models.dto.QuizDto;
import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.models.mapping.CycleAvoidingContext;
import com.mapquizzes.models.mapping.MapstructIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper
public interface QuizMapper {

    @MapstructIgnore
    default QuizDto mapEntityToDto(QuizEntity entity){
        return mapEntityToDto(entity, new CycleAvoidingContext());
    }

    QuizDto mapEntityToDto(QuizEntity entity, @Context CycleAvoidingContext context);
}
