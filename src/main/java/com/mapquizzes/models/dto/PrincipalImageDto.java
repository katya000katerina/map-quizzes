package com.mapquizzes.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrincipalImageDto {
    private byte[] bytes;
    private HttpHeaders headers;
}
