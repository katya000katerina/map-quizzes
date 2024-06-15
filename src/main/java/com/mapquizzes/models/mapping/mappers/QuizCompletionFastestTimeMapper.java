package com.mapquizzes.models.mapping.mappers;

import com.mapquizzes.models.dto.QuizCompletionFastestTimeDto;
import com.mapquizzes.models.entities.QuizCompletionFastestTimeEntity;
import org.mapstruct.Mapper;

@Mapper
public interface QuizCompletionFastestTimeMapper {
    QuizCompletionFastestTimeEntity mapDtoToEntity(QuizCompletionFastestTimeDto dto);

    QuizCompletionFastestTimeDto mapEntityToDto(QuizCompletionFastestTimeEntity entity);
}
