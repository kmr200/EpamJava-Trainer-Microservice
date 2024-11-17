package com.epam.xstack.gym.trainer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyRequiredFiledException extends RuntimeException {
    public EmptyRequiredFiledException(String message) {
        super(message);
    }
}
