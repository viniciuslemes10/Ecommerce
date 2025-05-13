package com.vinicius.ecommerce.exceptions;

import com.vinicius.ecommerce.exceptions.reponse.ApiException;
import org.springframework.http.HttpStatus;

public class ImageUploadException extends ApiException {
    public ImageUploadException() {
        super("Error uploading image to S3 bucket!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ImageUploadException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
