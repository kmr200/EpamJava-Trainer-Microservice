package com.epam.xstack.gym.trainer.service;

import com.epam.xstack.gym.trainer.dto.TrainerDTO;
import com.epam.xstack.gym.trainer.dto.TrainingSummary;
import com.epam.xstack.gym.trainer.exception.EmptyRequiredField;
import com.epam.xstack.gym.trainer.exception.TrainerByUsernameNotFound;
import com.epam.xstack.gym.trainer.jpa.entity.TrainerEntity;
import com.epam.xstack.gym.trainer.jpa.repository.TrainerRepository;
import com.epam.xstack.gym.trainer.mapper.TrainerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Service
public class TrainerService {


    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;

    public TrainerService(TrainerRepository trainerRepository, TrainerMapper trainerMapper) {
        this.trainerRepository = trainerRepository;
        this.trainerMapper = trainerMapper;
    }

    @Transactional
    // Returns if trainer already exists or creates a new one
    public TrainerDTO getOrCreate(
            String username,
            String firstName,
            String lastName,
            Boolean isActive
    ) {
        logger.debug("Get or create trainer by username {}", username);
        TrainerDTO trainer;
        //Check if the trainer already exists
        try {
            trainer = getTrainerByUsername(username);
        } catch (TrainerByUsernameNotFound e) {
            logger.info("Trainer with username {} does not exist, creating new profile", username);
            // Create if not
            trainer = createTrainer(
                    username, firstName, lastName, isActive
            );
        }

        logger.debug("Returning trainer {}", trainer);
        return trainer;
    }

    @CachePut(value = "trainerInfo", key = "#trainerUsername")
    public TrainerDTO addTraining(String trainerUsername, LocalDate trainingDate, Integer trainingDuration) {
        TrainerEntity trainer = getTrainer(trainerUsername);
        TrainingSummary trainingSummary = trainer.getTrainingSummary();
        trainingSummary = trainingSummary == null ? new TrainingSummary() : trainingSummary;

        trainingSummary.addTraining(trainingDate, trainingDuration);
        trainer.setTrainingSummary(trainingSummary);

        return trainerMapper.toDTO(
                trainerRepository.save(trainer)
        );
    }

    @CachePut(value = "trainerInfo", key = "#trainerUsername")
    public TrainerDTO deleteTraining(String trainerUsername, LocalDate trainingDate, Integer trainingDuration) {
        TrainerEntity trainer = getTrainer(trainerUsername);
        TrainingSummary trainingSummary = trainer.getTrainingSummary();
        trainingSummary = trainingSummary == null ? new TrainingSummary() : trainingSummary;

        trainingSummary.deleteTraining(trainingDate, trainingDuration);
        trainer.setTrainingSummary(trainingSummary);

        return trainerMapper.toDTO(trainerRepository.save(trainer));
    }

    @Cacheable(value = "trainerInfo", key = "#username")
    public TrainerDTO getTrainerByUsername(String username) {
        logger.debug("Get trainer by username {}", username);
        return trainerMapper.toDTO(getTrainer(username));
    }

    @CachePut(value = "trainerInfo", key = "#username")
    public TrainerDTO updateTrainer(
            String username,
            String firstName,
            String lastName,
            Boolean status
    ) {
        logger.debug("Update trainer by username {}", username);
        checkRequiredFields(username);

        TrainerEntity trainer = getTrainer(username);

        if (StringUtils.hasText(firstName)) trainer.setFirstName(firstName);
        if (StringUtils.hasText(lastName)) trainer.setLastName(lastName);
        if (status != null && !status.equals(trainer.getStatus())) {
            //If trainer is inactive, delete training's associated with the trainer
            if (!status) {
                trainer.setTrainingSummary(null);
            }
            trainer.setStatus(status);
        }

        return trainerMapper.toDTO(
                trainerRepository.save(trainer)
        );
    }

    @CachePut(value = "trainerInfo", key = "#username")
    public TrainerDTO createTrainer(
            String username,
            String firstName,
            String lastName,
            Boolean status
    ) {
        logger.debug("Create trainer with username {}", username);
        return trainerMapper.toDTO(createTrainerEntity(
                username, firstName, lastName, status
        ));
    }

    private TrainerEntity createTrainerEntity(
            String username,
            String firstName,
            String lastName,
            Boolean status
    ) {
        logger.debug("Create trainer with username {}", username);
        return trainerRepository.save(
                new TrainerEntity(username, firstName, lastName, status)
        );
    }

    private TrainerEntity getTrainer(String username) {
        logger.debug("Get trainer with username {}", username);
        return trainerRepository.findByTrainerUsernameIgnoreCase(username)
                .orElseThrow(() -> new TrainerByUsernameNotFound("Trainer with username " + username + " not found"));
    }

    private void checkRequiredFields(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                logger.error("One of the required fields is empty");
                throw new EmptyRequiredField("Some of the required fields are empty");
            }
        }
    }

}
