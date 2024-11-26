package com.epam.xstack.gym.trainer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Schema(description = "Response template for /api/v1/trainer-workload/username/trainings endpoint")
@NoArgsConstructor
@Getter
@Setter
public class GetTrainingsResponse {

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

}
