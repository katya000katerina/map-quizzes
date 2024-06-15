package com.mapquizzes.repositories.implementations;

import com.mapquizzes.models.entities.MistakeEntity;
import com.mapquizzes.models.entities.QuizCompletionFastestTimeEntity;
import com.mapquizzes.repositories.interfaces.MistakeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MistakeRepositoryImpl implements MistakeRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void saveOrUpdate(MistakeEntity entity) {
        em.merge(entity);
        em.flush();
    }

    @Override
    public Optional<MistakeEntity> getByUserId(Integer userId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MistakeEntity> cq = cb.createQuery(MistakeEntity.class);
        Root<MistakeEntity> root = cq.from(MistakeEntity.class);
        cq.select(root).where(cb.equal(root.get("userId"), userId));
        return em.createQuery(cq).getResultStream().findFirst();
    }
}
