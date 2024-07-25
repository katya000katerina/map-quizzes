package com.mapquizzes.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapquizzes.validation.annotations.UniqueUsername;
import com.mapquizzes.validation.groups.user.SignIn;
import com.mapquizzes.validation.groups.user.SignUp;
import jakarta.validation.constraints.NotBlank;

import java.time.OffsetDateTime;

public record UserDto(Integer id,
                      @NotBlank(message = "Username cannot be blank",
                              groups = {SignIn.class, SignUp.class})
                      @UniqueUsername(groups = SignUp.class)
                      String username,
                      @NotBlank(message = "Password cannot be blank",
                              groups = {SignIn.class, SignUp.class})
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
