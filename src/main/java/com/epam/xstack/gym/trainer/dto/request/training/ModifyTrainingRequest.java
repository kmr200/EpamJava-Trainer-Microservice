package com.epam.xstack.gym.trainer.dto.request.training;

import com.epam.xstack.gym.trainer.dto.request.Request;
import com.epam.xstack.gym.trainer.exception.EmptyRequiredFiledException;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

@Schema(description = "Template for create training requests")
@NoArgsConstructor
@Data
public class ModifyTrainingRequest implements Request {

    public ModifyTrainingRequest(
            String username,
            String firstName,
            String lastName,
            Boolean isActive,
            LocalDate trainingDate,
            Integer trainingDuration,
            ActionType actionType
    ) {
        this.firstName = firstName;
        this.isActive = isActive;
        this.lastName = lastName;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
        this.username = username;
        this.actionType = actionType;
    }

    private static final Logger logger = LoggerFactory.getLogger(ModifyTrainingRequest.class);

    @Schema(description = "Username of the trainer", example = "Michael.Wilson")
    @JsonProperty("username")
    private String username;

    @Schema(description = "First name of the trainer", example = "Michael")
    @JsonProperty("firstName")
    private String firstName;

    @Schema(description = "Last name of the trainer", example = "Wilson")
    @JsonProperty("lastName")
    private String lastName;

    @Schema(description = "Activity status of the trainer", example = "true")
    @JsonProperty("isActive")
    private Boolean isActive;

    @Schema(description = "Expected date for the training", example = "2024-09-10")
    @JsonProperty("trainingDate")
    private LocalDate trainingDate;

    @Schema(description = "Duration of the training in minutes", example = "30")
    @JsonProperty("trainingDuration")
    private Integer trainingDuration;

    @Schema(description = "Action to be taken: CREATE/DELETE", examples = {"CREATE", "DELETE"})
    @JsonProperty("actionType")
    private ActionType actionType;

    public Boolean getActive() {
        return isActive;
    }

    public ModifyTrainingRequest setActive(Boolean active) {
        isActive = active;
        return this;
    }

    public void deleteCheckRequiredFields() {
        if (username == null || username.isEmpty() ||
                actionType == null || trainingDate == null || trainingDuration == null) {
            logger.warn("Required fields for deleting training are missing");
            throw new EmptyRequiredFiledException("Required fields for deleting training are missing");
        }
    }

    @Override
    public void checkRequiredFields() {
        if (username == null || username.isEmpty() ||
                firstName == null || firstName.isEmpty() ||
                lastName == null || lastName.isEmpty() ||
                isActive == null || trainingDate == null ||
                trainingDuration == null || actionType == null) {
            logger.warn("Required fields are missing");
            throw new EmptyRequiredFiledException("Required fields are missing");
        }
    }
}
