package com.mapquizzes.models.dto;

import org.springframework.http.HttpHeaders;

public record PrincipalImageDto(byte[] bytes, HttpHeaders headers) {}
