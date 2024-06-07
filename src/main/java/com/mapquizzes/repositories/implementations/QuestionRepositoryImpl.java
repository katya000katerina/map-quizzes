package com.mapquizzes.repositories.implementations;

import com.mapquizzes.models.entities.QuestionEntity;
import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.repositories.interfaces.QuestionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public class QuestionRepositoryImpl implements QuestionRepository {
    @PersistenceContext
    EntityManager em;

    @Override
    public Stream<QuestionEntity> findAllByQuizId(Integer quizId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<QuestionEntity> cq = cb.createQuery(QuestionEntity.class);
        Root<QuestionEntity> root = cq.from(QuestionEntity.class);
        Join<QuestionEntity, QuizEntity> quizJoin = root.join("quizEntity");
        cq.select(root).where(cb.equal(quizJoin.get("id"), quizId));
        return em.createQuery(cq).getResultList().stream();
    }
}
