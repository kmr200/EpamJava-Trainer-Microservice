package com.epam.xstack.gym.trainer.controller.handler;

import com.epam.xstack.gym.trainer.dto.response.exception.ExceptionBodyTemplate;
import com.epam.xstack.gym.trainer.exception.EmptyRequiredFiledException;
import com.epam.xstack.gym.trainer.exception.TrainerByUsernameNotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = {EmptyRequiredFiledException.class})
    protected ResponseEntity<Object> handleEmptyRequiredFiled(EmptyRequiredFiledException ex, HttpServletRequest request) {

        logger.error("EmptyRequiredFiled Exception occurred: {}", ex.getMessage());

        return new ResponseEntity<>(
                new ExceptionBodyTemplate(
                        400,
                        "Bad Request",
                        ex.getMessage(),
                        request.getRequestURI()
                ), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = {TrainerByUsernameNotFound.class})
    protected ResponseEntity<Object> handleTrainerByUsernameNotFound(TrainerByUsernameNotFound ex, HttpServletRequest request) {

        logger.error("TrainerByUsernameNotFound Exception occurred: {}", ex.getMessage());

        return new ResponseEntity<>(
                new ExceptionBodyTemplate(
                        404,
                        "Not Found",
                        ex.getMessage(),
                        request.getRequestURI()
                ), HttpStatus.NOT_FOUND
        );
    }

}
