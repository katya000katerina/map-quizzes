package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.custom.NullDtoException;
import com.mapquizzes.exceptions.custom.NullIdException;
import com.mapquizzes.models.dto.QuizCompletionFastestTimeDto;
import com.mapquizzes.models.entities.QuizCompletionFastestTimeEntity;
import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.mapping.mappers.QuizCompletionFastestTimeMapper;
import com.mapquizzes.repositories.interfaces.QuizCompletionFastestTimeRepository;
import com.mapquizzes.services.interfaces.QuizCompletionFastestTimeService;
import com.mapquizzes.services.interfaces.QuizService;
import com.mapquizzes.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizCompletionFastestTimeServiceImpl implements QuizCompletionFastestTimeService {
    private final QuizCompletionFastestTimeRepository fastestTimeRepo;
    private final UserService userService;
    private final QuizService quizService;
    private final QuizCompletionFastestTimeMapper mapper;

    @Override
    @Transactional
    public void saveOrUpdate(QuizCompletionFastestTimeDto dto, Principal principal, Integer quizId) {
        if (dto == null) {
            throw new NullDtoException("MistakeDto is null");
        }
        if (principal == null) {
            throw new IllegalArgumentException("Principal is null");
        }
        if (quizId == null) {
            throw new NullIdException("Question id is null");
        }

        UserEntity userEntity = userService.getEntityByPrincipal(principal);
        Optional<QuizCompletionFastestTimeEntity> currEntityOp = fastestTimeRepo.getByUser(userEntity);

        if (currEntityOp.isEmpty() || currEntityOp.get().getTimeInMillis() > dto.getTimeInMillis()) {
            QuizCompletionFastestTimeEntity entity = mapper.mapDtoToEntity(dto);
            QuizEntity quizEntity = quizService.getEntityById(quizId);
            entity.setUser(userEntity);
            entity.setQuiz(quizEntity);
            fastestTimeRepo.saveOrUpdate(entity);
        }
    }
}
