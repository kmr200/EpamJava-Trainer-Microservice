package com.epam.xstack.gym.trainer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Schema(description = "Response template for /api/v1/trainer-workload/trainer/username/ endpoint")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class TrainerDTO {

    private String id;
    @Schema(description = "Username of the trainer", example = "Michael.Wilson")
    private String trainerUsername;
    @Schema(description = "First name of the trainer", example = "Michael")
    private String firstName;
    @Schema(description = "Last name of the trainer", example = "Wilson")
    private String lastName;
    @Schema(description = "Activity status of the trainer", example = "true")
    private Boolean status;
    @Schema(description = "Trainings summary")
    private TrainingSummary trainingSummary;

    public TrainerDTO(String trainerUsername, String firstName, String lastName, Boolean status, TrainingSummary trainingSummary) {
        this.trainerUsername = trainerUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.trainingSummary = trainingSummary;
    }

    public TrainingSummary getTrainingSummary() {
        return trainingSummary == null ? new TrainingSummary() : trainingSummary;
    }
}
