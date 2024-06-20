package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.custom.NullDtoException;
import com.mapquizzes.models.dto.QuizCompletionFastestTimeDto;
import com.mapquizzes.models.entities.QuizCompletionFastestTimeEntity;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.mapping.mappers.QuizCompletionFastestTimeMapper;
import com.mapquizzes.repositories.interfaces.QuizCompletionFastestTimeRepository;
import com.mapquizzes.repositories.interfaces.QuizRepository;
import com.mapquizzes.repositories.interfaces.UserRepository;
import com.mapquizzes.services.interfaces.QuizCompletionFastestTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizCompletionFastestTimeServiceImpl implements QuizCompletionFastestTimeService {
    private final QuizCompletionFastestTimeRepository fastestTimeRepo;
    private final UserRepository userRepo;
    private final QuizRepository quizRepo;
    private final QuizCompletionFastestTimeMapper mapper;

    @Override
    @Transactional
    public void saveOrUpdate(QuizCompletionFastestTimeDto dto, Integer userId, Integer quizId) {
        if (dto == null) {
            throw new NullDtoException("QuizCompletionFastestTimeDto is null");
        }
        UserEntity userEntity = userRepo.findById(userId).orElseThrow();
        Optional<QuizCompletionFastestTimeEntity> currEntityOp = fastestTimeRepo.getByUser(userEntity);
        if (currEntityOp.isEmpty() || currEntityOp.get().getTimeInMillis() > dto.getTimeInMillis()) {
            QuizCompletionFastestTimeEntity entity = mapper.mapDtoToEntity(dto);
            entity.setUser(userEntity);
            entity.setQuiz(quizRepo.findById(quizId).orElseThrow());
            fastestTimeRepo.saveOrUpdate(entity);
        }
    }
}