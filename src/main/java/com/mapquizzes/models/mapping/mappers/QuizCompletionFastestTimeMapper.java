package com.mapquizzes.models.mapping.mappers;

import com.mapquizzes.models.dto.QuizCompletionFastestTimeDto;
import com.mapquizzes.models.entities.QuizCompletionFastestTimeEntity;
import com.mapquizzes.models.mapping.CycleAvoidingContext;
import com.mapquizzes.models.mapping.MapstructIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper
public interface QuizCompletionFastestTimeMapper {

    @MapstructIgnore
    default QuizCompletionFastestTimeEntity mapDtoToEntity(QuizCompletionFastestTimeDto dto) {
        return mapDtoToEntity(dto, new CycleAvoidingContext());
    }

    @MapstructIgnore
    default QuizCompletionFastestTimeDto mapEntityToDto(QuizCompletionFastestTimeEntity entity) {
        return mapEntityToDto(entity, new CycleAvoidingContext());
    }

    QuizCompletionFastestTimeEntity mapDtoToEntity(QuizCompletionFastestTimeDto dto, @Context CycleAvoidingContext context);

    QuizCompletionFastestTimeDto mapEntityToDto(QuizCompletionFastestTimeEntity entity, @Context CycleAvoidingContext context);
}
