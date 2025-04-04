package com.epam.xstack.gym.trainer.controller;

import com.epam.xstack.gym.trainer.controller.docs.TrainerControllerDocs;
import com.epam.xstack.gym.trainer.dto.TrainerDTO;
import com.epam.xstack.gym.trainer.dto.response.GetTrainingsResponse;
import com.epam.xstack.gym.trainer.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/trainer-workload/trainer")
public class TrainerController implements TrainerControllerDocs {

    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
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
                        trainer.getStatus(),
                        trainer.getTrainingSummary().getSummary()
                ),
                HttpStatus.OK
        );
    }

}
