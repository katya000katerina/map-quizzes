package com.mapquizzes.services.implementations;

import com.mapquizzes.exceptions.custom.EntityNotFoundException;
import com.mapquizzes.models.entities.FastestTimeEntity;
import com.mapquizzes.models.entities.QuizEntity;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.repositories.interfaces.FastestTimeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Override
    public Page<FastestTimeEntity> findByQuizIdWithUserSortedByTimeAsc(Integer quizId, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<FastestTimeEntity> cq = cb.createQuery(FastestTimeEntity.class);
        Root<FastestTimeEntity> root = cq.from(FastestTimeEntity.class);
        Join<FastestTimeEntity, QuizEntity> quizJoin = root.join("quiz");
        cq.select(root).where(cb.equal(quizJoin.get("id"), quizId));
        cq.orderBy(cb.asc(root.get("timeInMillis")));
        List<FastestTimeEntity> result = em.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<FastestTimeEntity> countRoot = countQuery.from(FastestTimeEntity.class);
        Join<FastestTimeEntity, QuizEntity> quizCountJoin = countRoot.join("quiz");
        countQuery.select(cb.count(countRoot)).where(cb.equal(quizCountJoin.get("id"), quizId));

        Long count = em.createQuery(countQuery).getResultStream().findFirst().orElseThrow(
                () -> {
                    throw new EntityNotFoundException();
                }
        );

        return new PageImpl<>(result, pageable, count);
    }
}
