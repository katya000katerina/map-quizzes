package com.mapquizzes.services.implemenations;

import com.mapquizzes.config.CacheConfig;
import com.mapquizzes.exceptions.custom.EntityNotFoundException;
import com.mapquizzes.exceptions.custom.NullIdException;
import com.mapquizzes.models.dto.QuizDto;
import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.models.mapping.mappers.QuizMapper;
import com.mapquizzes.repositories.QuizRepository;
import com.mapquizzes.services.interfaces.QuizQuestionService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizQuestionServiceImpl implements QuizQuestionService {
    private final QuizRepository quizRepo;
    private final QuizMapper mapper;

    @Override
    @Cacheable(value = CacheConfig.QUIZ_QUESTION_CACHE_NAME, unless = "#result == null")
    public QuizDto getQuizByIdWithQuestions(Integer quizId) {
        if (quizId == null) {
            throw new NullIdException("Quiz id is null");
        }
        QuizEntity entity = quizRepo.findById(quizId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException(String.format("Quiz with id=%d was not found", quizId));
                });
        Hibernate.initialize(entity.getQuestions());
        return mapper.mapEntityToDto(entity);
    }
}
