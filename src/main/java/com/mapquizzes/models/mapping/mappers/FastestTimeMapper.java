package com.mapquizzes.models.mapping.mappers;

import com.mapquizzes.models.dto.GlobalRankingDto;
import com.mapquizzes.models.entities.FastestTimeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FastestTimeMapper {
    @Mapping(source = "user.username", target = "username")
    GlobalRankingDto mapEntityToGlobalRankingDto(FastestTimeEntity fastestTimeEntity);
}
