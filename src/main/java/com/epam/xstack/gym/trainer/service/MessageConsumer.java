package com.epam.xstack.gym.trainer.service;

import com.epam.xstack.gym.trainer.dto.TrainerDTO;
import com.epam.xstack.gym.trainer.dto.TrainingDTO;
import com.epam.xstack.gym.trainer.dto.request.trainer.UpdateTrainerRequest;
import com.epam.xstack.gym.trainer.dto.request.training.ActionType;
import com.epam.xstack.gym.trainer.dto.request.training.ModifyTrainingRequest;
import com.epam.xstack.gym.trainer.exception.EmptyRequiredField;
import com.epam.xstack.gym.trainer.jpa.entity.TrainerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public MessageConsumer(TrainerService trainerService, TrainingService trainingService) {
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    @Transactional
    @JmsListener(destination = "modify-training")
    public void receiveModifyTrainingMessage(ModifyTrainingRequest request) {
        logger.debug("Manage training request: {}", request);
        if (request.getActionType() == null) {
            throw new EmptyRequiredField("Action type not specified");
        }

        // Create training
        if (request.getActionType().equals(ActionType.CREATE)) {
            // Check required fields for creating training
            request.checkRequiredFields();

            logger.debug("Create training request: {}", request);
            //Save the trainer if it does not exist
            TrainerDTO trainer = trainerService.getOrCreate(
                    request.getUsername(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getActive()
            );
            logger.debug("Created/received trainer: {}", trainer);

            TrainingDTO training = trainingService.addTraining(
                    request.getUsername(),
                    request.getTrainingDate(),
                    request.getTrainingDuration()
            );
            logger.debug("Created training: {}", training);

        } else if (request.getActionType().equals(ActionType.DELETE)) {
            // Delete training

            request.deleteCheckRequiredFields();

            logger.debug("Delete training request: {}", request);

            TrainingDTO trainingDTO = trainingService.deleteTraining(
                    request.getUsername(),
                    request.getTrainingDate(),
                    request.getTrainingDuration()
            );

            logger.debug("Deleted training: {}", trainingDTO);

        }
    }

    @JmsListener(destination = "update-trainer")
    @Transactional
    public void receiveUpdateTrainerMessage(UpdateTrainerRequest request) {
        logger.debug("Update trainer message: {}", request);

        TrainerDTO updateTrainer = trainerService.updateTrainer(
                request.getUsername(),
                request.getFirstName(),
                request.getLastName(),
                request.getActive()
        );
    }

    @JmsListener(destination = "ActiveMQ.DLQ")
    @Transactional
    public void logDeadLetters(Object message) {
        logger.warn("Dead letter was found: {}", message);
    }
}
