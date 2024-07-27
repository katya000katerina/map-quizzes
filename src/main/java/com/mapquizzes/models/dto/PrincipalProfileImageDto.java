package com.mapquizzes.models.dto;

import org.springframework.http.HttpHeaders;

public record PrincipalProfileImageDto(byte[] bytes, HttpHeaders headers) {}
