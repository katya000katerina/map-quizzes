package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.custom.NullDtoException;
import com.mapquizzes.exceptions.custom.NullIdException;
import com.mapquizzes.models.dto.FastestTimeDto;
import com.mapquizzes.models.entities.FastestTimeEntity;
import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.mapping.mappers.FastestTimeMapper;
import com.mapquizzes.repositories.interfaces.FastestTimeRepository;
import com.mapquizzes.services.interfaces.FastestTimeService;
import com.mapquizzes.services.interfaces.QuizService;
import com.mapquizzes.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    @Transactional
    public void saveOrUpdate(FastestTimeDto dto, Principal principal, Integer quizId) {
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
        Optional<FastestTimeEntity> currEntityOp = fastestTimeRepo.getByUser(userEntity);

        if (currEntityOp.isEmpty() || currEntityOp.get().getTimeInMillis() > dto.getTimeInMillis()) {
            FastestTimeEntity entity = mapper.mapDtoToEntity(dto);
            QuizEntity quizEntity = quizService.getEntityById(quizId);
            entity.setUser(userEntity);
            entity.setQuiz(quizEntity);
            fastestTimeRepo.saveOrUpdate(entity);
        }
    }

    @Override
    public Page<FastestTimeDto> getDtoByQuizIdWithUserSortedByTimeAsc(Integer quizId, Pageable pageable) {
        return fastestTimeRepo
                .findByQuizIdWithUserSortedByTimeAsc(quizId, pageable)
                .map(mapper::mapEntityToDtoWithoutQuiz);
    }
}
