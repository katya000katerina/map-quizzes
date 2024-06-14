package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.custom.EntityNotFoundException;
import com.mapquizzes.exceptions.custom.NullIdException;
import com.mapquizzes.models.dto.QuizDto;
import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.models.mapping.mappers.QuizMapper;
import com.mapquizzes.repositories.interfaces.QuizRepository;
import com.mapquizzes.services.interfaces.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepo;
    private final QuizMapper mapper;

    @Override
    public QuizDto getById(Integer id) {
        if (id == null) {
            throw new NullIdException("Quiz id is null");
        }
        Optional<QuizEntity> entity = quizRepo.findById(id);
        if (entity.isPresent()) {
            return mapper.mapEntityToDto(entity.get());
        } else throw new EntityNotFoundException(String.format("Quiz entity with id %d not found", id));
    }

    @Override
    public Stream<QuizDto> getAll() {
        return quizRepo.findAll().map(mapper::mapEntityToDto).sorted(Comparator.comparing(QuizDto::getId));
    }
}
