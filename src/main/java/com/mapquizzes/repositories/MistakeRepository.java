package com.mapquizzes.repositories;

import com.mapquizzes.models.entities.MistakeEntity;
import com.mapquizzes.models.entities.compositekeys.MistakeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MistakeRepository extends JpaRepository<MistakeEntity, MistakeId> {
}
