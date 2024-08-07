package com.mapquizzes.services.implemenations;

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
    private final FastestTimeMapper fastestTimeMapper;

    @Transactional
    @Override
    public TestCompletionTimeDto saveOrUpdate(TestCompletionTimeDto complTimeDto, Principal principal) {
        UserEntity userEntity = userService.getEntityByPrincipal(principal);
        QuizEntity quizEntity = quizService.getEntityById(complTimeDto.quizId());
        Optional<FastestTimeEntity> currEntityOp = fastestTimeRepo.findById(new FastestTimeId(userEntity, quizEntity));

        Integer timeInMillis = complTimeDto.timeInMillis();
        FastestTimeEntity fstTimeEntity;
        if (currEntityOp.isPresent()) {
            fstTimeEntity = currEntityOp.get();
            if (fstTimeEntity.getTimeInMillis() > timeInMillis) {
                fstTimeEntity.setTimeInMillis(timeInMillis);
                fstTimeEntity = fastestTimeRepo.save(fstTimeEntity);
            }
        } else {
            fstTimeEntity = new FastestTimeEntity(userEntity, quizEntity, timeInMillis);
            fstTimeEntity = fastestTimeRepo.save(fstTimeEntity);
        }
        return fastestTimeMapper.mapEntityToTestCompletionTimeDto(fstTimeEntity);
    }

    @Transactional
    @Override
    public void deleteAllByUser(UserEntity userEntity) {
        fastestTimeRepo.deleteAllByUser(userEntity);
    }
}
