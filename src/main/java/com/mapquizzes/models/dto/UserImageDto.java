package com.mapquizzes.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserImageDto {
    private Integer id;
    @NotNull
    @NotBlank
    private String fileName;
    private byte[] bytes;
    @NotNull
    private UserDto user;
}
