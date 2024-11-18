package com.epam.xstack.gym.trainer.dto.request.training;

import com.epam.xstack.gym.trainer.dto.request.Request;
import com.epam.xstack.gym.trainer.exception.EmptyRequiredFiledException;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Objects;

@Schema(description = "Template for create training requests")
public class CreateTrainingRequest implements Request {

    public CreateTrainingRequest(String firstName, Boolean isActive, String lastName, LocalDate trainingDate, Integer trainingDuration, String username) {
        this.firstName = firstName;
        this.isActive = isActive;
        this.lastName = lastName;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
        this.username = username;
    }

    public CreateTrainingRequest() {}

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

    public String getFirstName() {
        return firstName;
    }

    public CreateTrainingRequest setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Boolean getActive() {
        return isActive;
    }

    public CreateTrainingRequest setActive(Boolean active) {
        isActive = active;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public CreateTrainingRequest setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public CreateTrainingRequest setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
        return this;
    }

    public Integer getTrainingDuration() {
        return trainingDuration;
    }

    public CreateTrainingRequest setTrainingDuration(Integer trainingDuration) {
        this.trainingDuration = trainingDuration;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public CreateTrainingRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateTrainingRequest that)) return false;
        return Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(isActive, that.isActive) && Objects.equals(getTrainingDate(), that.getTrainingDate()) && Objects.equals(getTrainingDuration(), that.getTrainingDuration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getFirstName(), getLastName(), isActive, getTrainingDate(), getTrainingDuration());
    }

    @Override
    public String toString() {
        return "CreateTrainingRequest{" +
                "firstName='" + firstName + '\'' +
                ", username='" + username + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActive=" + isActive +
                ", trainingDate=" + trainingDate +
                ", trainingDuration=" + trainingDuration +
                '}';
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
