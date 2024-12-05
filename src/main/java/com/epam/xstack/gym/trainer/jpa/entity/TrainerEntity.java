package com.epam.xstack.gym.trainer.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "trainer")
@NoArgsConstructor
@Data
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

}
