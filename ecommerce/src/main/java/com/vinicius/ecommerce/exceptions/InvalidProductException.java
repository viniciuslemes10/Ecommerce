package com.vinicius.ecommerce.exceptions;

import com.vinicius.ecommerce.exceptions.reponse.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidProductException extends ApiException {
    public InvalidProductException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
