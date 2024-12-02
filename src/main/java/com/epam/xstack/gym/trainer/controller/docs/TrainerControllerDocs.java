package com.epam.xstack.gym.trainer.controller.docs;

import com.epam.xstack.gym.trainer.dto.response.GetTrainingsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Trainer management")
public interface TrainerControllerDocs {

    @Operation(description = "Returns summary of trainings ordered by years and month", responses = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetTrainingsResponse.class)
                    )
            )
    })
    ResponseEntity<GetTrainingsResponse> getTrainings(@PathVariable("username") String username);

}
