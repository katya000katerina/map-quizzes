package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.custom.internalservererror.NullDtoException;
import com.mapquizzes.models.dto.TestCompletionTimeDto;
import com.mapquizzes.models.entities.FastestTimeEntity;
import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.entities.compositekeys.FastestTimeId;
import com.mapquizzes.models.mapping.mappers.FastestTimeMapper;
import com.mapquizzes.repositories.FastestTimeRepository;
import com.mapquizzes.services.interfaces.FastestTimeService;
import com.mapquizzes.services.interfaces.QuizService;
import com.mapquizzes.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FastestTimeServiceImpl implements FastestTimeService {
    private final FastestTimeRepository fastestTimeRepo;
    private final UserService userService;
    private final QuizService quizService;
    private final FastestTimeMapper mapper;

    @Transactional
    @Override
    public TestCompletionTimeDto saveOrUpdate(TestCompletionTimeDto dto, Principal principal) {
        if (dto == null) {
            throw new NullDtoException("TestCompletionTimeDto is null");
        }
        if (principal == null) {
            throw new IllegalArgumentException("Principal is null");
        }

        UserEntity userEntity = userService.getEntityByPrincipal(principal);
        QuizEntity quizEntity = quizService.getEntityById(dto.quizId());
        Optional<FastestTimeEntity> currEntityOp = fastestTimeRepo.findById(new FastestTimeId(userEntity, quizEntity));

        Integer timeInMillis = dto.timeInMillis();
        FastestTimeEntity entity;
        if (currEntityOp.isPresent()) {
            entity = currEntityOp.get();
            if (entity.getTimeInMillis() > timeInMillis) {
                entity.setTimeInMillis(timeInMillis);
                entity = fastestTimeRepo.save(entity);
            }
        } else {
            entity = new FastestTimeEntity(userEntity, quizEntity, timeInMillis);
            entity = fastestTimeRepo.save(entity);
        }
        return mapper.mapEntityToTestCompletionTimeDto(entity);
    }

    @Transactional
    @Override
    public void deleteAllByUser(UserEntity user) {
        fastestTimeRepo.deleteAllByUser(user);
    }
}
