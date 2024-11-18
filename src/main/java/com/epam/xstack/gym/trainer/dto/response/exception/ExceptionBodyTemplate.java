package com.epam.xstack.gym.trainer.dto.response.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Template for all the exception response bodies")
public class ExceptionBodyTemplate {

    @Schema(description = "Time stamp", example = "2024-09-24T10:45:00Z")
    private String timeStamp;
    @Schema(description = "Status code", example = "401")
    private int status;
    @Schema(description = "A brief description of the error", example = "Unauthorized request")
    private String error;
    @Schema(description = "Error message", example = "Wrong username or password")
    private String message;
    @Schema(description = "The API path were the error occurred", example = "/api/v1/trainees")
    private String path;

    public ExceptionBodyTemplate(int status, String error, String message, String path) {
        this.timeStamp = LocalDateTime.now().toString();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public ExceptionBodyTemplate(String error, String message, String path, int status, String timeStamp) {
        this.error = error;
        this.message = message;
        this.path = path;
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public String getError() {
        return error;
    }

    public ExceptionBodyTemplate setError(String error) {
        this.error = error;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ExceptionBodyTemplate setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getPath() {
        return path;
    }

    public ExceptionBodyTemplate setPath(String path) {
        this.path = path;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public ExceptionBodyTemplate setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public ExceptionBodyTemplate setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }
}
