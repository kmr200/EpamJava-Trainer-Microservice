package com.epam.xstack.gym.trainer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@Data
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

}
