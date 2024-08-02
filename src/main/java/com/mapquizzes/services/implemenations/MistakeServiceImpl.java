package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.custom.internalservererror.NullDtoException;
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

    @Transactional
    @Override
    public MistakeDto saveOrUpdate(MistakeDto dto, Principal principal) {
        if (dto == null) {
            throw new NullDtoException("MistakeDto is null");
        }
        if (principal == null) {
            throw new IllegalArgumentException("Principal is null");
        }

        UserEntity userEntity = userService.getEntityByPrincipal(principal);
        QuestionEntity questionEntity = questionService.getEntityById(dto.questionId());

        Optional<MistakeEntity> currMistakeEntityOp = mistakeRepo.findById(new MistakeId(userEntity, questionEntity));
        MistakeEntity mistakeEntity;
        if (currMistakeEntityOp.isPresent()) {
            mistakeEntity = currMistakeEntityOp.get();
            mistakeEntity.setNumberOfMistakes(mistakeEntity.getNumberOfMistakes() + dto.numberOfMistakes());
        } else {
            mistakeEntity = new MistakeEntity(userEntity, questionEntity, dto.numberOfMistakes());
        }
        mistakeEntity = mistakeRepo.save(mistakeEntity);
        return new MistakeDto(mistakeEntity.getQuestion().getId(), mistakeEntity.getNumberOfMistakes());
    }

    @Transactional
    @Override
    public void deleteAllByUser(UserEntity user) {
        mistakeRepo.deleteAllByUser(user);
    }

    @Transactional
    @Override
    public void deleteByQuestionIdAndPrincipal(Integer questionId, Principal principal) {
        UserEntity user = userService.getEntityByPrincipal(principal);
        mistakeRepo.deleteByQuestionIdAndUser(questionId, user);
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
                    mistake.getQuestion().getId(),
                    mistake.getQuestion().getQuestion(),
                    mistake.getNumberOfMistakes()
            );
            quizMistakes.mistakes().add(principalMistakeDto);
        }

        return new ArrayList<>(quizMistakesMap.values());
    }
}
