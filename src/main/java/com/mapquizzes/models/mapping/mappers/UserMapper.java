package com.mapquizzes.models.mapping.mappers;

import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.models.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDto mapEntityToDto(UserEntity entity);

    UserEntity mapDtoToEntity(UserDto dto);
}
