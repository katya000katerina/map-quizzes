package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.custom.NullDtoException;
import com.mapquizzes.exceptions.custom.NullIdException;
import com.mapquizzes.models.dto.MistakeDto;
import com.mapquizzes.models.entities.MistakeEntity;
import com.mapquizzes.models.entities.QuestionEntity;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.entities.compositekeys.MistakeId;
import com.mapquizzes.repositories.interfaces.MistakeRepository;
import com.mapquizzes.services.interfaces.MistakeService;
import com.mapquizzes.services.interfaces.QuestionService;
import com.mapquizzes.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MistakeServiceImpl implements MistakeService {
    private final MistakeRepository mistakeRepo;
    private final UserService userService;
    private final QuestionService questionService;

    @Override
    @Transactional
    public void saveOrUpdate(MistakeDto dto, Principal principal, Integer questionId) {
        if (dto == null) {
            throw new NullDtoException("MistakeDto is null");
        }
        if (principal == null) {
            throw new IllegalArgumentException("Principal is null");
        }
        if (questionId == null) {
            throw new NullIdException("Question id is null");
        }

        UserEntity userEntity = userService.getEntityByPrincipal(principal);
        QuestionEntity questionEntity = questionService.getEntityById(questionId);

        Optional<MistakeEntity> currMistakeEntityOp = mistakeRepo.findById(new MistakeId(userEntity, questionEntity));
        MistakeEntity mistakeEntity;
        if (currMistakeEntityOp.isPresent()) {
            mistakeEntity = currMistakeEntityOp.get();
            mistakeEntity.setNumberOfMistakes(mistakeEntity.getNumberOfMistakes() + dto.getNumberOfMistakes());
        } else {
            mistakeEntity = new MistakeEntity(userEntity, questionEntity, dto.getNumberOfMistakes());
        }
        mistakeRepo.saveOrUpdate(mistakeEntity);
    }
}
