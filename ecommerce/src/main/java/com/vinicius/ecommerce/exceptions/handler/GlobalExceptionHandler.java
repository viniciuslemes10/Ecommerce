package com.vinicius.ecommerce.exceptions.handler;

import com.vinicius.ecommerce.exceptions.reponse.ApiException;
import com.vinicius.ecommerce.exceptions.reponse.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ExceptionResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        var response = new ExceptionResponse(
                ex.getStatus().getReasonPhrase(),
                ex.getStatus().value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        var response = new ExceptionResponse(
                "Internal Server Error",
                500,
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(500).body(response);
    }
}
