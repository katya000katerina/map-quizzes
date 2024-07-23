package com.mapquizzes.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.time.OffsetDateTime;

public record UserDto(Integer id,
                      @NotBlank String username,
                      @NotBlank
                      @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
                      String password,
                      OffsetDateTime createdAt) {
    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password=********" +
                ", createdAt=" + createdAt +
                '}';
    }
}
