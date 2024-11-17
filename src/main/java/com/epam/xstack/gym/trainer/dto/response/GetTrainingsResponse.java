package com.epam.xstack.gym.trainer.dto.response;

import java.util.Map;

public class GetTrainingsResponse {

    public GetTrainingsResponse() {}

    public GetTrainingsResponse(String trainerUsername, String trainerFirstName, String trainerLastName, Boolean trainerStatus, Map<Integer, Map<Integer, Integer>> trainings) {
        this.trainerFirstName = trainerFirstName;
        this.trainerLastName = trainerLastName;
        this.trainerStatus = trainerStatus;
        this.trainerUsername = trainerUsername;
        this.trainings = trainings;
    }

    String trainerUsername;
    String trainerFirstName;
    String trainerLastName;
    Boolean trainerStatus;
    Map<Integer, Map<Integer, Integer>> trainings;

    public String getTrainerFirstName() {
        return trainerFirstName;
    }

    public GetTrainingsResponse setTrainerFirstName(String trainerFirstName) {
        this.trainerFirstName = trainerFirstName;
        return this;
    }

    public String getTrainerLastName() {
        return trainerLastName;
    }

    public GetTrainingsResponse setTrainerLastName(String trainerLastName) {
        this.trainerLastName = trainerLastName;
        return this;
    }

    public Boolean getTrainerStatus() {
        return trainerStatus;
    }

    public GetTrainingsResponse setTrainerStatus(Boolean trainerStatus) {
        this.trainerStatus = trainerStatus;
        return this;
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public GetTrainingsResponse setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
        return this;
    }

    public Map<Integer, Map<Integer, Integer>> getTrainings() {
        return trainings;
    }

    public GetTrainingsResponse setTrainings(Map<Integer, Map<Integer, Integer>> trainings) {
        this.trainings = trainings;
        return this;
    }
}
