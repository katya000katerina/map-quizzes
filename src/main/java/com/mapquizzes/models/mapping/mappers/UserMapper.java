package com.mapquizzes.models.mapping.mappers;

import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.models.dto.UserImageDto;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.entities.UserImageEntity;
import com.mapquizzes.models.mapping.CycleAvoidingContext;
import com.mapquizzes.models.mapping.MapstructIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    @MapstructIgnore
    default UserDto mapEntityToDto(UserEntity entity) {
        return mapEntityToDto(entity, new CycleAvoidingContext());
    }

    @MapstructIgnore
    default UserEntity mapDtoToEntity(UserDto dto) {
        return mapDtoToEntity(dto, new CycleAvoidingContext());
    }

    UserDto mapEntityToDto(UserEntity entity, @Context CycleAvoidingContext context);

    UserEntity mapDtoToEntity(UserDto dto, @Context CycleAvoidingContext context);

    UserImageDto mapImageEntityToImageDto(UserImageEntity imageEntity,  @Context CycleAvoidingContext context);
    UserImageEntity mapImageDtoToImageEntity(UserImageDto imageDto,  @Context CycleAvoidingContext context);
}
