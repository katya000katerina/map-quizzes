package com.mapquizzes.repositories.implementations;

import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.repositories.interfaces.QuizRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public class QuizRepositoryImpl implements QuizRepository {
    @PersistenceContext
    EntityManager em;

    @Override
    public Stream<QuizEntity> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<QuizEntity> cq = cb.createQuery(QuizEntity.class);
        Root<QuizEntity> root = cq.from(QuizEntity.class);
        cq.select(root);
        return em.createQuery(cq).getResultList().stream();
    }
}
