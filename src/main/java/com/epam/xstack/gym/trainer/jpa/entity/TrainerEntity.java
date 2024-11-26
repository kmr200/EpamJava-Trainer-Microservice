package com.epam.xstack.gym.trainer.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "trainer")
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TrainerEntity {

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
}
