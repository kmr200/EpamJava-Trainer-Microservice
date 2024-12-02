package com.epam.xstack.gym.trainer.controller.docs;

import com.epam.xstack.gym.trainer.dto.request.training.ModifyTrainingRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Training management")
public interface TrainingControllerDocs {

    @Operation(summary = "Register new training", responses = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
    ResponseEntity<Object> manageTraining(@RequestBody ModifyTrainingRequest request);

}
