package com.epam.xstack.gym.trainer.controller;

import com.epam.xstack.gym.trainer.controller.docs.TrainingControllerDocs;
import com.epam.xstack.gym.trainer.dto.TrainerDTO;
import com.epam.xstack.gym.trainer.dto.TrainingDTO;
import com.epam.xstack.gym.trainer.dto.request.training.CreateTrainingRequest;
import com.epam.xstack.gym.trainer.dto.response.GetTrainingsResponse;
import com.epam.xstack.gym.trainer.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/v1/trainer-workload", produces = {"application/JSON"})
public class TrainingController implements TrainingControllerDocs {

    private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);

    private final TrainerService trainerService;

    public TrainingController(TrainerService trainerService) {
        this.trainerService = trainerService;
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

        TrainingDTO training = trainerService.addTraining(
                request.getUsername(),
                request.getTrainingDate(),
                request.getTrainingDuration()
        );
        logger.debug("Created training: {}", training);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{username}/trainings")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetTrainingsResponse> getTrainings(@PathVariable("username") String username) {
        logger.debug("Get trainings request: {}", username);

        TrainerDTO trainer = trainerService.getTrainerByUsername(username);
        logger.debug("Got trainer: {}", trainer);

        return new ResponseEntity<>(
                new GetTrainingsResponse(
                        username,
                        trainer.getFirstName(),
                        trainer.getLastName(),
                        trainer.getActive(),
                        trainerService.getSortedTrainingsByTrainer(username)
                ),
                HttpStatus.OK
        );
    }

}
