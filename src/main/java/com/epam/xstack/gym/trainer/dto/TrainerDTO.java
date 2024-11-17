package com.epam.xstack.gym.trainer.dto;

import java.util.Objects;

public class TrainerDTO {

    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;

    public TrainerDTO() {}

    public TrainerDTO(String firstName, Boolean isActive, String lastName, String username) {
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
