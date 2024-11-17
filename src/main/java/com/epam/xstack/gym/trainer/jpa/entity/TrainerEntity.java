package com.epam.xstack.gym.trainer.jpa.entity;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "trainer")
public class TrainerEntity {

    public TrainerEntity() {}

    public TrainerEntity(String username, String firstName, String lastName, Boolean isActive) {
        this.firstName = firstName;
        this.isActive = isActive;
        this.lastName = lastName;
        this.username = username;
    }

    @Id
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "trainer", fetch = FetchType.EAGER)
    private Set<TrainingEntity> trainings;

    public String getFirstName() {
        return firstName;
    }

    public TrainerEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Boolean getActive() {
        return isActive;
    }

    public TrainerEntity setActive(Boolean active) {
        isActive = active;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public TrainerEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public TrainerEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public Set<TrainingEntity> getTrainings() {
        return trainings;
    }

    public TrainerEntity setTrainings(Set<TrainingEntity> trainings) {
        this.trainings = trainings;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrainerEntity that)) return false;
        return Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getFirstName(), getLastName(), isActive);
    }

    @Override
    public String toString() {
        return "TrainerEntity{" +
                "isActive=" + isActive +
                ", username='" + username + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}
