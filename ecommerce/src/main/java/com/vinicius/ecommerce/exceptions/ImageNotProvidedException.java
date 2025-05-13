package com.vinicius.ecommerce.exceptions;

import com.vinicius.ecommerce.exceptions.reponse.ApiException;
import org.springframework.http.HttpStatus;

public class ImageNotProvidedException extends ApiException {
    public ImageNotProvidedException() {
        super("Image not provided!", HttpStatus.BAD_REQUEST);
    }
}
