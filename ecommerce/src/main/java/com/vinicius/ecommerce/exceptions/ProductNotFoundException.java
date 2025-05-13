package com.vinicius.ecommerce.exceptions;

import com.vinicius.ecommerce.exceptions.reponse.ApiException;
import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends ApiException {
    public ProductNotFoundException() {
        super("Product not found!", HttpStatus.NOT_FOUND);
    }
}
