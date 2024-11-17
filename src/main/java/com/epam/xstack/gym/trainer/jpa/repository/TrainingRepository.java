package com.epam.xstack.gym.trainer.jpa.repository;

import com.epam.xstack.gym.trainer.jpa.entity.TrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<TrainingEntity, String> {

    @Query("SELECT tr FROM TrainingEntity tr JOIN tr.trainer t WHERE t.username = :username ORDER BY function('YEAR', tr.trainingDate) DESC, function('MONTH', tr.trainingDate) DESC")
    List<TrainingEntity> findAllTrainingsByTrainerSortedByYearAndMonth(@Param("username") String username);

}
