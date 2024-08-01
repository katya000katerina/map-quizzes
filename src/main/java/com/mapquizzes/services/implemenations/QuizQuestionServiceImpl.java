package com.mapquizzes.services.implemenations;

import com.mapquizzes.config.CacheConfig;
import com.mapquizzes.exceptions.custom.internalservererror.EntityNotFoundException;
import com.mapquizzes.exceptions.custom.badrequest.InvalidIdException;
import com.mapquizzes.exceptions.custom.internalservererror.NullIdException;
import com.mapquizzes.models.dto.QuizDto;
import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.mapping.mappers.QuizMapper;
import com.mapquizzes.repositories.QuizRepository;
import com.mapquizzes.services.interfaces.QuizQuestionService;
import com.mapquizzes.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class QuizQuestionServiceImpl implements QuizQuestionService {
    private final QuizRepository quizRepo;
    private final QuizMapper mapper;
    private final UserService userService;

    @Override
    @Cacheable(value = CacheConfig.QUIZ_QUESTION_CACHE_NAME, unless = "#result == null")
    public QuizDto getQuizByIdWithQuestions(Integer quizId) {
        checkQuizId(quizId);

        QuizEntity entity = quizRepo.findById(quizId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException(String.format("Quiz with id=%d was not found", quizId));
                });
        Hibernate.initialize(entity.getQuestions());
        return mapper.mapEntityToDto(entity);
    }

    @Override
    public QuizDto getMistakesQuizByIdWithQuestions(Integer quizId, Principal principal) {
        checkQuizId(quizId);

        if (principal == null) {
            throw new IllegalArgumentException("Principal is null");
        }

        UserEntity user = userService.getEntityByPrincipal(principal);

        QuizEntity entity = quizRepo.findMistakesQuizByIdAndUser(quizId, user)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException(String.format("Quiz with id=%d was not found", quizId));
                });

        return mapper.mapEntityToDto(entity);
    }

    private void checkQuizId(Integer quizId) {
        if (quizId == null) {
            throw new NullIdException("Quiz id is null");
        }
        if (!quizRepo.existsById(quizId)) {
            throw new InvalidIdException(String.format("Quiz with id=%d doesn't exist", quizId));
        }
    }
}
