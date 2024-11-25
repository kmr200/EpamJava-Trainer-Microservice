package com.epam.xstack.gym.trainer.dto.request.trainer;

import com.epam.xstack.gym.trainer.dto.request.Request;
import com.epam.xstack.gym.trainer.exception.EmptyRequiredField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Schema(description = "Template for requests at PUT /api/v1/trainer-workload/trainer/username/")
public class UpdateTrainerRequest implements Request {

    @Schema(description = "First name of the trainer", example = "Michael")
    private String firstName;
    @Schema(description = "Last name of the trainer", example = "Wilson")
    private String lastName;
    @Schema(description = "Activity status of the trainer", example = "true")
    private Boolean isActive;

    public UpdateTrainerRequest() {}

    public UpdateTrainerRequest(String firstName, String lastName, Boolean isActive) {
        this.firstName = firstName;
        this.isActive = isActive;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public UpdateTrainerRequest setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Boolean getActive() {
        return isActive;
    }

    public UpdateTrainerRequest setActive(Boolean active) {
        isActive = active;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UpdateTrainerRequest setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateTrainerRequest that)) return false;
        return Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), isActive);
    }

    @Override
    public String toString() {
        return "UpdateTrainerRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public void checkRequiredFields() {
    }
}
