package com.mapquizzes.models.mapping.mappers;

import com.mapquizzes.models.dto.MistakeDto;
import com.mapquizzes.models.entities.MistakeEntity;
import org.mapstruct.Mapper;

@Mapper
public interface MistakeMapper {
    MistakeEntity mapDtoToEntity(MistakeDto dto);

    MistakeDto mapEntityToDto(MistakeEntity entity);
}
