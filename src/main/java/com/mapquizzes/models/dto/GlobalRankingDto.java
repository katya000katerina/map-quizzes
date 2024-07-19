package com.mapquizzes.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalRankingDto {
    private String username;
    private Integer timeInMillis;
}
