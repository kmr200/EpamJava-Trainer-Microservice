package com.epam.xstack.gym.trainer.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(description = "Response template for /api/v1/trainer-workload/trainer/username/ endpoint")
public class TrainerDTO {

    @Schema(description = "Username of the trainer", example = "Michael.Wilson")
    private String username;
    @Schema(description = "First name of the trainer", example = "Michael")
    private String firstName;
    @Schema(description = "Last name of the trainer", example = "Wilson")
    private String lastName;
    @Schema(description = "Activity status of the trainer", example = "true")
    private Boolean isActive;

    public TrainerDTO() {}

    public TrainerDTO(String username, String firstName, String lastName, Boolean isActive) {
        this.firstName = firstName;
        this.isActive = isActive;
        this.lastName = lastName;
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public TrainerDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Boolean getActive() {
        return isActive;
    }

    public TrainerDTO setActive(Boolean active) {
        isActive = active;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public TrainerDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public TrainerDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrainerDTO that)) return false;
        return Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getFirstName(), getLastName(), isActive);
    }

    @Override
    public String toString() {
        return "TrainerDTO{" +
                "firstName='" + firstName + '\'' +
                ", username='" + username + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
