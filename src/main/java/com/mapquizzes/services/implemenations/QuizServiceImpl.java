package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.custom.internalservererror.EntityNotFoundException;
import com.mapquizzes.exceptions.custom.internalservererror.NullIdException;
import com.mapquizzes.models.dto.QuizDto;
import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.models.mapping.mappers.QuizMapper;
import com.mapquizzes.repositories.QuizRepository;
import com.mapquizzes.services.interfaces.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepo;
    private final QuizMapper mapper;

    @Override
    public QuizEntity getEntityById(Integer id) {
        if (id == null) {
            throw new NullIdException("Quiz id is null");
        }
        return quizRepo.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(String.format("Quiz with id=%d was not found", id));
        });
    }

    @Override
    public QuizDto getDtoById(Integer id) {
        return mapper.mapEntityToDtoWithoutQuestions(getEntityById(id));
    }

    @Override
    public Stream<QuizDto> getAllDto() {
        return quizRepo.findAll().stream().map(mapper::mapEntityToDto).sorted(Comparator.comparing(QuizDto::id));
    }
}
