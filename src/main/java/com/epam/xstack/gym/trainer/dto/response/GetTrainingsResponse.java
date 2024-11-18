package com.epam.xstack.gym.trainer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Response template for /api/v1/trainer-workload/username/trainings endpoint")
public class GetTrainingsResponse {

    public GetTrainingsResponse() {
    }

    public GetTrainingsResponse(String trainerUsername, String trainerFirstName, String trainerLastName, Boolean trainerStatus, Map<Integer, Map<Integer, Integer>> trainings) {
        this.trainerFirstName = trainerFirstName;
        this.trainerLastName = trainerLastName;
        this.trainerStatus = trainerStatus;
        this.trainerUsername = trainerUsername;
        this.trainings = trainings;
    }

    @Schema(description = "Trainers username", example = "Michael.Wilson")
    String trainerUsername;
    @Schema(description = "Trainers first name", example = "Michael")
    String trainerFirstName;
    @Schema(description = "Trainers last name", example = "Wilson")
    String trainerLastName;
    @Schema(description = "Trainers activity status", example = "true")
    Boolean trainerStatus;
    @Schema(
            description = "Trainings ordered by years and month",
            example = """
        {
            "2024": {
                "9": 60,
                "10": 120
            },
            "2025": {
                "1": 30
            }
        }
    """
    )
    Map<Integer, Map<Integer, Integer>> trainings;


    public String getTrainerFirstName() {
        return trainerFirstName;
    }

    public GetTrainingsResponse setTrainerFirstName(String trainerFirstName) {
        this.trainerFirstName = trainerFirstName;
        return this;
    }

    public String getTrainerLastName() {
        return trainerLastName;
    }

    public GetTrainingsResponse setTrainerLastName(String trainerLastName) {
        this.trainerLastName = trainerLastName;
        return this;
    }

    public Boolean getTrainerStatus() {
        return trainerStatus;
    }

    public GetTrainingsResponse setTrainerStatus(Boolean trainerStatus) {
        this.trainerStatus = trainerStatus;
        return this;
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public GetTrainingsResponse setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
        return this;
    }

    public Map<Integer, Map<Integer, Integer>> getTrainings() {
        return trainings;
    }

    public GetTrainingsResponse setTrainings(Map<Integer, Map<Integer, Integer>> trainings) {
        this.trainings = trainings;
        return this;
    }
}
