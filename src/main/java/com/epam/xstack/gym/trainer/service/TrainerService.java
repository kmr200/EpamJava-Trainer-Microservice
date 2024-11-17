package com.epam.xstack.gym.trainer.service;

import com.epam.xstack.gym.trainer.dto.TrainerDTO;
import com.epam.xstack.gym.trainer.dto.TrainingDTO;
import com.epam.xstack.gym.trainer.exception.TrainerByUsernameNotFound;
import com.epam.xstack.gym.trainer.jpa.entity.TrainerEntity;
import com.epam.xstack.gym.trainer.jpa.entity.TrainingEntity;
import com.epam.xstack.gym.trainer.jpa.repository.TrainerRepository;
import com.epam.xstack.gym.trainer.jpa.repository.TrainingRepository;
import com.epam.xstack.gym.trainer.mapper.TrainerMapper;
import com.epam.xstack.gym.trainer.mapper.TrainingMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;

    public TrainerService(TrainerRepository trainerRepository, TrainingRepository trainingRepository, TrainerMapper trainerMapper, TrainingMapper trainingMapper) {
        this.trainerRepository = trainerRepository;
        this.trainingRepository = trainingRepository;
        this.trainerMapper = trainerMapper;
        this.trainingMapper = trainingMapper;
    }

    public TrainerDTO getOrCreate(
            String username,
            String firstName,
            String lastName,
            Boolean isActive
    ) {
        TrainerEntity trainer;
        //Check if the trainer already exists
        try {
            trainer = getTrainer(username);
        } catch (TrainerByUsernameNotFound e) {
            // Create if not
            trainer = createTrainer(
                    username, firstName, lastName, isActive
            );
        }

        return trainerMapper.toDTO(trainer);
    }

    public TrainingDTO addTraining (
            String trainerUsername,
            LocalDate trainingDate,
            Integer trainingDuration
    ) throws TrainerByUsernameNotFound {
        TrainerEntity trainer;

        try {
            trainer = getTrainer(trainerUsername);
        } catch (TrainerByUsernameNotFound e) {
            throw new TrainerByUsernameNotFound("Could not create training. Trainer with username " + trainerUsername + " not found.");
        }

        TrainingEntity training = new TrainingEntity(
                trainingDate,
                trainingDuration,
                trainer
        );
        trainingRepository.save(training);

        return trainingMapper.toDto(training);
    }

    public TrainerDTO getTrainerByUsername(String username) {
        return trainerMapper.toDTO(getTrainer(username));
    }

    public Map<Integer, Map<Integer, Integer>> getSortedTrainingsByTrainer(String trainerUsername) {
        List<TrainingEntity> trainings = trainingRepository.findAllTrainingsByTrainerSortedByYearAndMonth(trainerUsername);
        return trainings.stream()
                .collect(Collectors.groupingBy(
                        //Group trainings by year
                        training -> training.getTrainingDate().getYear(),
                        Collectors.groupingBy(
                                //Group trainings by month
                                training -> training.getTrainingDate().getMonthValue(),
                                //Calculate summary training duration
                                Collectors.summingInt(TrainingEntity::getTrainingDuration)
                        )
                ));
    }

    private TrainerEntity createTrainer(
            String username,
            String firstName,
            String lastName,
            Boolean isActive
    ) {
        return trainerRepository.save(
                new TrainerEntity(username, firstName, lastName, isActive)
        );
    }

    private TrainerEntity getTrainer(String username) {
        return trainerRepository.findById(username)
                .orElseThrow(() -> new TrainerByUsernameNotFound("Trainer with username " + username + " not found"));
    }

}
