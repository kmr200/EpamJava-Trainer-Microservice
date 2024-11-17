package com.epam.xstack.gym.trainer.jpa.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "training")
public class TrainingEntity {

    public TrainingEntity() {}

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

    public TrainerEntity getTrainer() {
        return trainer;
    }

    public TrainingEntity setTrainer(TrainerEntity trainer) {
        this.trainer = trainer;
        return this;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public TrainingEntity setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
        return this;
    }

    public Integer getTrainingDuration() {
        return trainingDuration;
    }

    public TrainingEntity setTrainingDuration(Integer trainingDuration) {
        this.trainingDuration = trainingDuration;
        return this;
    }

    public String getTrainingUUID() {
        return trainingUUID;
    }

    public TrainingEntity setTrainingUUID(String trainingUUID) {
        this.trainingUUID = trainingUUID;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrainingEntity that)) return false;
        return Objects.equals(getTrainingUUID(), that.getTrainingUUID()) && Objects.equals(getTrainer(), that.getTrainer()) && Objects.equals(getTrainingDate(), that.getTrainingDate()) && Objects.equals(getTrainingDuration(), that.getTrainingDuration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTrainingUUID(), getTrainer(), getTrainingDate(), getTrainingDuration());
    }

    @Override
    public String toString() {
        return "TrainingEntity{" +
                "trainer=" + trainer +
                ", trainingUUID='" + trainingUUID + '\'' +
                ", trainingDate=" + trainingDate +
                ", trainingDuration=" + trainingDuration +
                '}';
    }
}
