package com.mapquizzes.models.mapping.mappers;

import com.mapquizzes.models.dto.FastestTimeDto;
import com.mapquizzes.models.entities.FastestTimeEntity;
import com.mapquizzes.models.mapping.CycleAvoidingContext;
import com.mapquizzes.models.mapping.MapstructIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper
public interface FastestTimeMapper {

    @MapstructIgnore
    default FastestTimeEntity mapDtoToEntity(FastestTimeDto dto) {
        return mapDtoToEntity(dto, new CycleAvoidingContext());
    }

    @MapstructIgnore
    default FastestTimeDto mapEntityToDto(FastestTimeEntity entity) {
        return mapEntityToDto(entity, new CycleAvoidingContext());
    }

    FastestTimeEntity mapDtoToEntity(FastestTimeDto dto, @Context CycleAvoidingContext context);

    FastestTimeDto mapEntityToDto(FastestTimeEntity entity, @Context CycleAvoidingContext context);
}
