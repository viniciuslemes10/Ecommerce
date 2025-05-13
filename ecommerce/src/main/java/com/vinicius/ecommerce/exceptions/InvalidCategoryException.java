package com.vinicius.ecommerce.exceptions;

import com.vinicius.ecommerce.exceptions.reponse.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidCategoryException extends ApiException {

    public InvalidCategoryException() {
        super("Invalid category field!", HttpStatus.BAD_REQUEST);
    }
}
