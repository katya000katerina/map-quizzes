package com.mapquizzes.repositories;

import com.mapquizzes.models.entities.FastestTimeEntity;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.entities.compositekeys.FastestTimeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FastestTimeRepository extends JpaRepository<FastestTimeEntity, FastestTimeId>,
        PagingAndSortingRepository<FastestTimeEntity, FastestTimeId> {

    @Query(value = "select t from FastestTimeEntity t join fetch t.user where t.quiz.id = :quizId",
            countQuery = "select count(t) from FastestTimeEntity t where t.quiz.id = :quizId")
    Page<FastestTimeEntity> findAllByQuizId(Integer quizId, Pageable pageable);

    @Query(value = """
                select 
                    q.name as quizname,
                    (select count(*) + 1 from quizzes.quizzes_completion_fastest_time f2 
                     where f2.quiz_id = f1.quiz_id and f2.time_millis < f1.time_millis) as rank
                from quizzes.quizzes_completion_fastest_time f1
                join quizzes.quizzes q on f1.quiz_id = q.id
                where f1.user_id = :userId
                order by q.name
            """, nativeQuery = true)
    List<Object[]> findUserRankings(@Param("userId") Integer userId);

    void deleteAllByUser(UserEntity user);
}
