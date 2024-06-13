package com.mapquizzes.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class UserDto {
    private Integer id;
    @NotNull @NotBlank
    private String username;
    @NotNull @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private OffsetDateTime createdAt;
    private UserImageDto userImage;
}
