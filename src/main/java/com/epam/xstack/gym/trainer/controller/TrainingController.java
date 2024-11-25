package com.epam.xstack.gym.trainer.controller;

import com.epam.xstack.gym.trainer.controller.docs.TrainingControllerDocs;
import com.epam.xstack.gym.trainer.dto.TrainerDTO;
import com.epam.xstack.gym.trainer.dto.TrainingDTO;
import com.epam.xstack.gym.trainer.dto.request.training.CreateTrainingRequest;
import com.epam.xstack.gym.trainer.dto.response.GetTrainingsResponse;
import com.epam.xstack.gym.trainer.service.TrainerService;
import com.epam.xstack.gym.trainer.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/v1/trainer-workload/training", produces = {"application/JSON"})
public class TrainingController implements TrainingControllerDocs {

    private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);

    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public TrainingController(TrainerService trainerService, TrainingService trainingService) {
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    @PostMapping(value = "/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> createTraining(@RequestBody CreateTrainingRequest request) {
        logger.debug("Create training request: {}", request);

        request.checkRequiredFields();

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

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
