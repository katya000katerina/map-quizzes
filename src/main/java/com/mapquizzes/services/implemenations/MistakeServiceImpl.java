package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.custom.NullDtoException;
import com.mapquizzes.exceptions.custom.NullIdException;
import com.mapquizzes.models.dto.MistakeDto;
import com.mapquizzes.models.dto.PrincipalQuizMistakesDto;
import com.mapquizzes.models.entities.MistakeEntity;
import com.mapquizzes.models.entities.QuestionEntity;
import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.entities.compositekeys.MistakeId;
import com.mapquizzes.repositories.MistakeRepository;
import com.mapquizzes.services.interfaces.MistakeService;
import com.mapquizzes.services.interfaces.QuestionService;
import com.mapquizzes.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MistakeServiceImpl implements MistakeService {
    private final MistakeRepository mistakeRepo;
    private final UserService userService;
    private final QuestionService questionService;

    @Override
    @Transactional
    public void saveOrUpdate(MistakeDto dto, Principal principal, Integer questionId) {
        if (dto == null) {
            throw new NullDtoException("MistakeDto is null");
        }
        if (principal == null) {
            throw new IllegalArgumentException("Principal is null");
        }
        if (questionId == null) {
            throw new NullIdException("Question id is null");
        }

        UserEntity userEntity = userService.getEntityByPrincipal(principal);
        QuestionEntity questionEntity = questionService.getEntityById(questionId);

        Optional<MistakeEntity> currMistakeEntityOp = mistakeRepo.findById(new MistakeId(userEntity, questionEntity));
        MistakeEntity mistakeEntity;
        if (currMistakeEntityOp.isPresent()) {
            mistakeEntity = currMistakeEntityOp.get();
            mistakeEntity.setNumberOfMistakes(mistakeEntity.getNumberOfMistakes() + dto.getNumberOfMistakes());
        } else {
            mistakeEntity = new MistakeEntity(userEntity, questionEntity, dto.getNumberOfMistakes());
        }
        mistakeRepo.save(mistakeEntity);
    }

    public List<PrincipalQuizMistakesDto> getMistakesForPrincipal(Principal principal) {
        UserEntity user = userService.getEntityByPrincipal(principal);

        List<MistakeEntity> mistakes = mistakeRepo.getAllByUser(user);

        Map<Integer, PrincipalQuizMistakesDto> quizMistakesMap = new HashMap<>();

        for (MistakeEntity mistake : mistakes) {
            QuizEntity quiz = mistake.getQuestion().getQuiz();
            PrincipalQuizMistakesDto quizMistakes = quizMistakesMap.computeIfAbsent(
                    quiz.getId(),
                    id -> new PrincipalQuizMistakesDto(id, quiz.getName(), new ArrayList<>())
            );

            PrincipalQuizMistakesDto.PrincipalMistakeDto principalMistakeDto = new PrincipalQuizMistakesDto.PrincipalMistakeDto(
                    mistake.getQuestion().getQuestion(),
                    mistake.getNumberOfMistakes()
            );
            quizMistakes.mistakes().add(principalMistakeDto);
        }

        return new ArrayList<>(quizMistakesMap.values());
    }
}
