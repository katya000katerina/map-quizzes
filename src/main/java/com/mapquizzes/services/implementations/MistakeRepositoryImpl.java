package com.mapquizzes.services.implementations;

import com.mapquizzes.models.entities.MistakeEntity;
import com.mapquizzes.models.entities.compositekeys.MistakeId;
import com.mapquizzes.repositories.interfaces.MistakeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    public Optional<MistakeEntity> findById(MistakeId mistakeId) {
        return Optional.ofNullable(em.find(MistakeEntity.class, mistakeId));
    }
}
