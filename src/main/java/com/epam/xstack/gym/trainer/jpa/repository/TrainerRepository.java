package com.epam.xstack.gym.trainer.jpa.repository;

import com.epam.xstack.gym.trainer.jpa.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<TrainerEntity, String> {
}
