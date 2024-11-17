package com.epam.xstack.gym.trainer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TrainerByUsernameNotFound extends RuntimeException{
    public TrainerByUsernameNotFound(String message) {
        super(message);
    }
}
