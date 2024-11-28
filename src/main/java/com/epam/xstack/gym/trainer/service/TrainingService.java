package com.epam.xstack.gym.trainer.service;

import com.epam.xstack.gym.trainer.dto.TrainingDTO;
import com.epam.xstack.gym.trainer.exception.TrainerByUsernameNotFound;
import com.epam.xstack.gym.trainer.exception.TrainingNotFound;
import com.epam.xstack.gym.trainer.jpa.entity.TrainerEntity;
import com.epam.xstack.gym.trainer.jpa.entity.TrainingEntity;
import com.epam.xstack.gym.trainer.jpa.repository.TrainerRepository;
import com.epam.xstack.gym.trainer.jpa.repository.TrainingRepository;
import com.epam.xstack.gym.trainer.mapper.TrainingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;
    private final TrainerRepository trainerRepository;

    public TrainingService(TrainingRepository trainingRepository, TrainingMapper trainingMapper, TrainerRepository trainerRepository) {
        this.trainingRepository = trainingRepository;
        this.trainingMapper = trainingMapper;
        this.trainerRepository = trainerRepository;
    }

    public TrainingDTO addTraining (
            String trainerUsername,
            LocalDate trainingDate,
            Integer trainingDuration
    ) throws TrainerByUsernameNotFound {
        logger.debug("Add training {}", trainerUsername);
        TrainerEntity trainer;

        try {
            trainer = getTrainer(trainerUsername);
        } catch (TrainerByUsernameNotFound e) {
            logger.warn("Trainer with username {} does not exist", trainerUsername);
            throw new TrainerByUsernameNotFound("Could not create training. Trainer with username " + trainerUsername + " not found.");
        }

        TrainingEntity training = new TrainingEntity(
                trainingDate,
                trainingDuration,
                trainer
        );
        trainingRepository.save(training);

        logger.debug("Added training: {}", training);
        return trainingMapper.toDto(training);
    }

    public Map<Integer, Map<Integer, Integer>> getSortedTrainingsByTrainer(String trainerUsername) {
        logger.debug("Get sorted trainings by username {}", trainerUsername);
        List<TrainingEntity> trainings = trainingRepository.findAllTrainingsByTrainer(trainerUsername, LocalDate.now());
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

    public TrainingDTO deleteTraining(
            String trainerUsername,
            LocalDate trainingDate,
            Integer trainingDuration
    ) {
        TrainerEntity trainer = getTrainer(trainerUsername);

        List<TrainingEntity> trainingList = trainingRepository.findByTrainerAndTrainingDateAndTrainingDuration(
                trainerUsername,
                trainingDate,
                trainingDuration
        );

        if (trainingList.isEmpty()) {
            throw new TrainingNotFound(
                    "Training for trainer: " + trainerUsername + ", with the following date: "
                            + trainingDate + " and duration: " + trainingDuration + " not found.");
        }

        TrainingEntity training = trainingList.get(0);
        trainingRepository.delete(training);

        return trainingMapper.toDto(training);
    }

    private TrainerEntity getTrainer(String username) {
        logger.debug("Get trainer with username {}", username);
        return trainerRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new TrainerByUsernameNotFound("Trainer with username " + username + " not found"));
    }

}
