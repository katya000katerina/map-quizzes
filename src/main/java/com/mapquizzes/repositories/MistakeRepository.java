package com.mapquizzes.repositories;

import com.mapquizzes.models.entities.MistakeEntity;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.entities.compositekeys.MistakeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MistakeRepository extends JpaRepository<MistakeEntity, MistakeId> {
    @Query("select m from MistakeEntity m join fetch m.question qs join fetch qs.quiz qz where m.user = :user")
    List<MistakeEntity> getAllByUser(UserEntity user);

}
