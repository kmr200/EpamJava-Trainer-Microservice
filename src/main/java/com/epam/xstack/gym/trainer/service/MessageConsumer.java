package com.epam.xstack.gym.trainer.service;

import com.epam.xstack.gym.trainer.dto.TrainerDTO;
import com.epam.xstack.gym.trainer.dto.request.trainer.UpdateTrainerRequest;
import com.epam.xstack.gym.trainer.dto.request.training.ActionType;
import com.epam.xstack.gym.trainer.dto.request.training.ModifyTrainingRequest;
import com.epam.xstack.gym.trainer.exception.EmptyRequiredField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile("!test")
public class MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    private final TrainerService trainerService;

    public MessageConsumer(TrainerService trainerService) {
        this.trainerService = trainerService;
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
                    request.getStatus()
            );
            logger.debug("Created/received trainer: {}", trainer);

            TrainerDTO updatedTrainer = trainerService.addTraining(
                    request.getUsername(),
                    request.getTrainingDate(),
                    request.getTrainingDuration()
            );
            logger.debug("Created training for the following trainer: {}", updatedTrainer);

        } else if (request.getActionType().equals(ActionType.DELETE)) {
            // Delete training

            request.deleteCheckRequiredFields();

            logger.debug("Delete training request: {}", request);

            TrainerDTO trainer = trainerService.deleteTraining(
                    request.getUsername(),
                    request.getTrainingDate(),
                    request.getTrainingDuration()
            );

            logger.debug("Deleted training for trainer: {}", trainer);

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

    @JmsListener(destination = "DLQ.modify-training")
    @Transactional
    public void processModifyTrainingDLQ(ModifyTrainingRequest request) {
        // Retry once more
        try {
            receiveModifyTrainingMessage(request);
        } catch (Exception e) {
            //In case of failure log the message
            logger.error("Modify training request has failed: {}", request);
        }
    }

    @JmsListener(destination = "DLQ.update-trainer")
    @Transactional
    public void processUpdateTrainerDLQ(UpdateTrainerRequest request) {
        //Retry once more
        try {
            receiveUpdateTrainerMessage(request);
        } catch (Exception e) {
            logger.error("Update training request has failed: {}", request);
        }
    }

}
