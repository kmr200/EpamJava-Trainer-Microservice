package com.epam.xstack.gym.trainer.jpa.repository;

import com.epam.xstack.gym.trainer.jpa.entity.TrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<TrainingEntity, String> {

    @Query("SELECT tr FROM TrainingEntity tr JOIN tr.trainer t WHERE LOWER(t.username) = LOWER(:username) AND tr.trainingDate >= :startDate ORDER BY function('YEAR', tr.trainingDate) DESC, function('MONTH', tr.trainingDate) DESC")
    List<TrainingEntity> findAllTrainingsByTrainer(@Param("username") String username, LocalDate startDate);

    @Modifying
    @Query("DELETE FROM TrainingEntity tr WHERE LOWER(tr.trainer.username) = LOWER(:username)")
    void deleteAllByTrainer (@Param("username") String username);

    @Query("SELECT tr FROM TrainingEntity tr WHERE LOWER(tr.trainer.username) = LOWER(:trainerUsername) AND tr.trainingDate = :trainingDate AND tr.trainingDuration = :duration")
    List<TrainingEntity> findByTrainerAndTrainingDateAndTrainingDuration(String trainerUsername, LocalDate trainingDate, Integer duration);

}
