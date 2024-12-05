package com.epam.xstack.gym.trainer.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class TrainingDTO {

    private String trainingUUID;
    private TrainerDTO trainer;
    private LocalDate trainingDate;
    private Integer trainingDuration;

    public TrainingDTO(TrainerDTO trainer, LocalDate trainingDate, Integer trainingDuration, String trainingId) {
        this.trainer = trainer;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
        this.trainingUUID = trainingId;
    }

    public TrainingDTO setUUID(String trainingUUID) {
        this.trainingUUID = trainingUUID;
        return this;
    }

    public TrainingDTO setTrainingUUID(String trainingUUID) {
        this.trainingUUID = trainingUUID;
        return this;
    }
}
