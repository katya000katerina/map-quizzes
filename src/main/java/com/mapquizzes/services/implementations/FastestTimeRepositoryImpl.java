package com.mapquizzes.services.implementations;

import com.mapquizzes.models.entities.FastestTimeEntity;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.repositories.interfaces.FastestTimeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class FastestTimeRepositoryImpl implements FastestTimeRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void saveOrUpdate(FastestTimeEntity entity) {
        em.merge(entity);
        em.flush();
    }

    @Override
    public Optional<FastestTimeEntity> getByUser(UserEntity userEntity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<FastestTimeEntity> cq = cb.createQuery(FastestTimeEntity.class);
        Root<FastestTimeEntity> root = cq.from(FastestTimeEntity.class);
        cq.select(root).where(cb.equal(root.get("user"), userEntity));
        return em.createQuery(cq).getResultStream().findFirst();
    }
}
