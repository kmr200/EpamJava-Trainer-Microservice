package com.epam.xstack.gym.trainer.controller.docs;

import com.epam.xstack.gym.trainer.dto.request.training.CreateTrainingRequest;
import com.epam.xstack.gym.trainer.dto.response.GetTrainingsResponse;
import com.epam.xstack.gym.trainer.dto.response.exception.ExceptionBodyTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Training management")
public interface TrainingControllerDocs {

    @Operation(summary = "Register new training", responses = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
    ResponseEntity<String> createTraining(@RequestBody CreateTrainingRequest request);

}
