package com.epam.xstack.gym.trainer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Response template for /api/v1/trainer-workload/trainer/username/ endpoint")
@NoArgsConstructor
@Data
public class TrainerDTO {

    @Schema(description = "Username of the trainer", example = "Michael.Wilson")
    private String username;
    @Schema(description = "First name of the trainer", example = "Michael")
    private String firstName;
    @Schema(description = "Last name of the trainer", example = "Wilson")
    private String lastName;
    @Schema(description = "Activity status of the trainer", example = "true")
    private Boolean isActive;

    public TrainerDTO(String username, String firstName, String lastName, Boolean isActive) {
        this.firstName = firstName;
        this.isActive = isActive;
        this.lastName = lastName;
        this.username = username;
    }
}
