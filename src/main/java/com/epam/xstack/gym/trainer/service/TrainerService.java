package com.epam.xstack.gym.trainer.service;

import com.epam.xstack.gym.trainer.dto.TrainerDTO;
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
import java.util.HashMap;
import java.util.Map;

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

    @Transactional
    public TrainerDTO addTraining(String trainerUsername, LocalDate trainingDate, Integer trainingDuration) {
        TrainerEntity trainer = getTrainer(trainerUsername);
        Map<Integer, Map<Integer, Integer>> trainingSummary = trainer.getTrainingSummary();
        trainingSummary = trainingSummary == null ? new HashMap<Integer, Map<Integer, Integer>>() : trainingSummary;

        trainingSummary.computeIfAbsent(trainingDate.getYear(), year -> new HashMap<>())
                .merge(trainingDate.getMonthValue(), trainingDuration, Integer::sum);

        trainer.setTrainingSummary(trainingSummary);

        return trainerMapper.toDTO(
                trainerRepository.save(trainer)
        );
    }

    @Transactional
    public TrainerDTO deleteTraining(String trainerUsername, LocalDate trainingDate, Integer trainingDuration) {
        TrainerEntity trainer = getTrainer(trainerUsername);
        Map<Integer, Map<Integer, Integer>> trainingSummary = trainer.getTrainingSummary();
        trainingSummary = trainingSummary == null ? new HashMap<Integer, Map<Integer, Integer>>() : trainingSummary;

        trainingSummary.computeIfPresent(trainingDate.getYear(), (year, monthSummary) -> {
            monthSummary.computeIfPresent(trainingDate.getMonthValue(), (month, summary) -> {
                // Reduce training duration or remove the entry if it becomes zero
                int updatedSummary = summary - trainingDuration;
                return updatedSummary > 0 ? updatedSummary : null; // Remove if <= 0
            });

            // Remove the year entry if the month summary becomes empty
            return monthSummary.isEmpty() ? null : monthSummary;
        });

        return trainerMapper.toDTO(trainerRepository.save(trainer));
    }

    @Cacheable(value = "trainerInfo", key = "#username")
    public TrainerDTO getTrainerByUsername(String username) {
        logger.debug("Get trainer by username {}", username);
        return trainerMapper.toDTO(getTrainer(username));
    }

    @CachePut(value = "trainerInfo", key = "#username")
    @Transactional
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
