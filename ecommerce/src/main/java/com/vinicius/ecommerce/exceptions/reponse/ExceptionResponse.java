package com.vinicius.ecommerce.exceptions.reponse;

import java.time.LocalDateTime;

public class ExceptionResponse {
    private final String error;
    private final int status;
    private final String message;
    private final String path;
    private final LocalDateTime timestamp;

    public ExceptionResponse(String error, int status, String message, String path) {
        this.error = error;
        this.status = status;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    public String getError() {
        return error;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
