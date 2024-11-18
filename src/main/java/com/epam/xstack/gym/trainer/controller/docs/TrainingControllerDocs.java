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

@Tag(name = "Trainers training reports")
public interface TrainingControllerDocs {

    @Operation(summary = "Register new training", responses = {
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionBodyTemplate.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject("""
                                    {
                                      "timeStamp": "2024-09-24T10:45:00Z",
                                      "status": 403,
                                      "error": "Blocked User",
                                      "message": "User is blocked due to multiple failed login attempts",
                                      "path": "/api/v1/change-login"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "200"
            )
    })
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<String> createTraining(@RequestBody CreateTrainingRequest request);

    @Operation(description = "Returns summary of trainings ordered by years and month", responses = {
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionBodyTemplate.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject("""
                                    {
                                      "timeStamp": "2024-09-24T10:45:00Z",
                                      "status": 403,
                                      "error": "Blocked User",
                                      "message": "User is blocked due to multiple failed login attempts",
                                      "path": "/api/v1/change-login"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetTrainingsResponse.class)
                    )
            )
    })
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<GetTrainingsResponse> getTrainings(@PathVariable("username") String username);

}
