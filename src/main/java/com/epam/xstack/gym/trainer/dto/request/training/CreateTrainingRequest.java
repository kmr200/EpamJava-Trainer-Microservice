package com.epam.xstack.gym.trainer.dto.request.training;

import com.epam.xstack.gym.trainer.dto.request.Request;
import com.epam.xstack.gym.trainer.exception.EmptyRequiredFiledException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Objects;

@Schema(description = "Template for create training requests")
@NoArgsConstructor
@Data
public class CreateTrainingRequest implements Request {

    public CreateTrainingRequest(String username, String firstName, String lastName, Boolean isActive, LocalDate trainingDate, Integer trainingDuration) {
        this.firstName = firstName;
        this.isActive = isActive;
        this.lastName = lastName;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
        this.username = username;
    }

    private static final Logger logger = LoggerFactory.getLogger(CreateTrainingRequest.class);

    @Schema(description = "Username of the trainer", example = "Michael.Wilson")
    private String username;
    @Schema(description = "First name of the trainer", example = "Michael")
    private String firstName;
    @Schema(description = "Last name of the trainer", example = "Wilson")
    private String lastName;
    @Schema(description = "Activity status of the trainer", example = "true")
    private Boolean isActive;
    @Schema(description = "Expected date for the training", example = "2024-09-10")
    private LocalDate trainingDate;
    @Schema(description = "Duration of the training in minutes", example = "30")
    private Integer trainingDuration;

    public Boolean getActive() {
        return isActive;
    }

    public CreateTrainingRequest setActive(Boolean active) {
        isActive = active;
        return this;
    }

    @Override
    public void checkRequiredFields() {
        if (username == null || username.isEmpty() ||
                firstName == null || firstName.isEmpty() ||
                lastName == null || lastName.isEmpty() ||
                isActive == null || trainingDate == null || trainingDuration == null) {
            logger.warn("Required fields are missing");
            throw new EmptyRequiredFiledException("Required fields are missing");
        }
    }
}
