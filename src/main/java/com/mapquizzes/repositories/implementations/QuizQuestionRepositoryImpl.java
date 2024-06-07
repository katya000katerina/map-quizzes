package com.mapquizzes.repositories.implementations;

import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.models.entities.QuizQuestionEntity;
import com.mapquizzes.repositories.interfaces.QuizQuestionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public class QuizQuestionRepositoryImpl implements QuizQuestionRepository {
    @PersistenceContext
    EntityManager em;

    @Override
    public Stream<QuizQuestionEntity> findAllByQuizId(Integer quizId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<QuizQuestionEntity> cq = cb.createQuery(QuizQuestionEntity.class);
        Root<QuizQuestionEntity> root = cq.from(QuizQuestionEntity.class);
        Join<QuizQuestionEntity, QuizEntity> quizJoin = root.join("quizEntity");
        cq.select(root).where(cb.equal(quizJoin.get("id"), quizId));
        return em.createQuery(cq).getResultList().stream();
    }
}
