package com.mapquizzes.services.implemenations;

import com.mapquizzes.models.dto.QuizDto;
import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.models.mapping.mappers.QuizMapper;
import com.mapquizzes.repositories.interfaces.QuizRepository;
import com.mapquizzes.services.interfaces.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepo;
    private final QuizMapper mapper;

    @Override
    public Stream<QuizDto> getAll() {
        List<QuizDto> entities = quizRepo.findAll().map(mapper::mapEntityToDto).collect(Collectors.toList());
        return quizRepo.findAll().map(mapper::mapEntityToDto).sorted(Comparator.comparing(QuizDto::getId));
    }
}
