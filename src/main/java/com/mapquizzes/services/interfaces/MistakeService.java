package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.MistakeDto;

public interface MistakeService {
    void saveOrUpdate(MistakeDto dto);
}
