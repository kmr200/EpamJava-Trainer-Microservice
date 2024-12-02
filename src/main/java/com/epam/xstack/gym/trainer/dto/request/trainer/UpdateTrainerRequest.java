package com.epam.xstack.gym.trainer.dto.request.trainer;

import com.epam.xstack.gym.trainer.dto.request.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Schema(description = "Template for requests at PUT /api/v1/trainer-workload/trainer/username/")
@NoArgsConstructor
@Data
public class UpdateTrainerRequest implements Request {

    @Schema(description = "Username of the trainer", example = "Michael.Wilson")
    @NotBlank
    private String username;
    @Schema(description = "First name of the trainer", example = "Michael")
    private String firstName;
    @Schema(description = "Last name of the trainer", example = "Wilson")
    private String lastName;
    @Schema(description = "Activity status of the trainer", example = "true")
    private Boolean isActive;

    public UpdateTrainerRequest(
            String username,
            String firstName,
            String lastName,
            Boolean isActive
    ) {
        this.firstName = firstName;
        this.isActive = isActive;
        this.lastName = lastName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public UpdateTrainerRequest setActive(Boolean active) {
        isActive = active;
        return this;
    }

    @Override
    public void checkRequiredFields() {
    }
}
