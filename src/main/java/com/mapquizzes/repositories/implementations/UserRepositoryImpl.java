package com.mapquizzes.repositories.implementations;

import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.repositories.interfaces.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserEntity> cq = cb.createQuery(UserEntity.class);
        Root<UserEntity> root = cq.from(UserEntity.class);
        cq.select(root).where(cb.equal(root.get("username"), username));
        return em.createQuery(cq).getResultStream().findFirst();
    }

    @Override
    public Optional<UserEntity> save(UserEntity entity) {
        em.persist(entity);
        em.flush();
        return Optional.of(entity);
    }

    @Override
    public Optional<UserEntity> findById(Integer userId) {
        return Optional.ofNullable(em.find(UserEntity.class, userId));
    }
}
