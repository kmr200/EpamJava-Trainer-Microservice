package com.epam.xstack.gym.trainer.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class TrainingDTO {

    private String trainingUUID;
    private TrainerDTO trainer;
    private LocalDate trainingDate;
    private Integer trainingDuration;

    public TrainingDTO() {}

    public TrainingDTO(TrainerDTO trainer, LocalDate trainingDate, Integer trainingDuration, String trainingId) {
        this.trainer = trainer;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
        this.trainingUUID = trainingId;
    }

    public TrainerDTO getTrainer() {
        return trainer;
    }

    public TrainingDTO setTrainer(TrainerDTO trainer) {
        this.trainer = trainer;
        return this;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public TrainingDTO setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
        return this;
    }

    public Integer getTrainingDuration() {
        return trainingDuration;
    }

    public TrainingDTO setTrainingDuration(Integer trainingDuration) {
        this.trainingDuration = trainingDuration;
        return this;
    }

    public String getTrainingUUID() {
        return trainingUUID;
    }

    public TrainingDTO setTrainingUUID(String trainingUUID) {
        this.trainingUUID = trainingUUID;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrainingDTO that)) return false;
        return Objects.equals(getTrainingUUID(), that.getTrainingUUID()) && Objects.equals(getTrainer(), that.getTrainer()) && Objects.equals(getTrainingDate(), that.getTrainingDate()) && Objects.equals(getTrainingDuration(), that.getTrainingDuration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTrainingUUID(), getTrainer(), getTrainingDate(), getTrainingDuration());
    }

    @Override
    public String toString() {
        return "TrainingDTO{" +
                "trainer=" + trainer +
                ", trainingUUID='" + trainingUUID + '\'' +
                ", trainingDate=" + trainingDate +
                ", trainingDuration=" + trainingDuration +
                '}';
    }
}
