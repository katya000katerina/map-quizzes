package com.mapquizzes.repositories.implementations;

import com.mapquizzes.models.entities.QuizCompletionFastestTimeEntity;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.repositories.interfaces.QuizCompletionFastestTimeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class QuizCompletionFastestTimeRepositoryImpl implements QuizCompletionFastestTimeRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void saveOrUpdate(QuizCompletionFastestTimeEntity entity) {
        em.merge(entity);
        em.flush();
    }

    @Override
    public Optional<QuizCompletionFastestTimeEntity> getByUser(UserEntity userEntity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<QuizCompletionFastestTimeEntity> cq = cb.createQuery(QuizCompletionFastestTimeEntity.class);
        Root<QuizCompletionFastestTimeEntity> root = cq.from(QuizCompletionFastestTimeEntity.class);
        cq.select(root).where(cb.equal(root.get("user"), userEntity));
        return em.createQuery(cq).getResultStream().findFirst();
    }
}
