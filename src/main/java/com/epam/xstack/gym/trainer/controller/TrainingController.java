package com.epam.xstack.gym.trainer.controller;

import com.epam.xstack.gym.trainer.dto.TrainerDTO;
import com.epam.xstack.gym.trainer.dto.request.training.CreateTrainingRequest;
import com.epam.xstack.gym.trainer.dto.response.GetTrainingsResponse;
import com.epam.xstack.gym.trainer.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/v1/trainer-workload", produces = {"application/JSON"})
public class TrainingController {

    private final TrainerService trainerService;

    public TrainingController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping(value = "/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> createTraining(@RequestBody CreateTrainingRequest request) {
        request.checkRequiredFields();

        //Save the trainer if it does not exist
        trainerService.getOrCreate(
                request.getUsername(),
                request.getFirstName(),
                request.getLastName(),
                request.getActive()
        );


        trainerService.addTraining(
                request.getUsername(),
                request.getTrainingDate(),
                request.getTrainingDuration()
        );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{username}/trainings")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetTrainingsResponse> getTrainings(@PathVariable("username") String username) {
        TrainerDTO trainer = trainerService.getTrainerByUsername(username);

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
