package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.custom.NullDtoException;
import com.mapquizzes.models.dto.MistakeDto;
import com.mapquizzes.models.entities.MistakeEntity;
import com.mapquizzes.models.mapping.mappers.MistakeMapper;
import com.mapquizzes.repositories.interfaces.MistakeRepository;
import com.mapquizzes.services.interfaces.MistakeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MistakeServiceImpl implements MistakeService {
    private final MistakeRepository mistakeRepo;
    private final MistakeMapper mapper;

    @Override
    @Transactional
    public void saveOrUpdate(MistakeDto dto) {
        if (dto == null){
            throw new NullDtoException("MistakeDto is null");
        }
        Optional<MistakeEntity> currEntityOp = mistakeRepo.getByUserId(dto.getUserId());
        currEntityOp.ifPresent(mistakeEntity ->
                dto.setNumberOfMistakes(mistakeEntity.getNumberOfMistakes() + dto.getNumberOfMistakes()));
        MistakeEntity entity = mapper.mapDtoToEntity(dto);
        mistakeRepo.saveOrUpdate(entity);
    }
}
