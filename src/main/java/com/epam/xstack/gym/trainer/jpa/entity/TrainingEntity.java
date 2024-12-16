package com.epam.xstack.gym.trainer.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "training")
@NoArgsConstructor
@Data
public class TrainingEntity {

    public TrainingEntity(LocalDate trainingDate, Integer trainingDuration, TrainerEntity trainerEntity) {
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
        this.trainer = trainerEntity;
    }

    @Id
    @Column(name = "training_uuid", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String trainingUUID;

    @ManyToOne
    @JoinColumn(name = "trainer_username")
    private TrainerEntity trainer;

    @Column(name = "training_date", nullable = false)
    private LocalDate trainingDate;

    @Column(name = "training_duration", nullable = false)
    private Integer trainingDuration;

}
