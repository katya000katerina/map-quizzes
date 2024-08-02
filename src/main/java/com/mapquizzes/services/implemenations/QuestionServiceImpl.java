package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.custom.badrequest.InvalidIdException;
import com.mapquizzes.exceptions.custom.internalservererror.EntityNotFoundException;
import com.mapquizzes.exceptions.custom.internalservererror.NullIdException;
import com.mapquizzes.models.entities.QuestionEntity;
import com.mapquizzes.repositories.QuestionRepository;
import com.mapquizzes.services.interfaces.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepo;

    @Override
    public QuestionEntity getEntityById(Integer id) {
        if (id == null) {
            throw new NullIdException("Question id is null");
        }
        if (!questionRepo.existsById(id)) {
            throw new InvalidIdException(String.format("Question with id=%d doesn't exists", id));
        }

        return questionRepo.findById(id).orElseThrow(() ->{
            throw new EntityNotFoundException(String.format("Question with id=%d was not found", id));
        });
    }
}
