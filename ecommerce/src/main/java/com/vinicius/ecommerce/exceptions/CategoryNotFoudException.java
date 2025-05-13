package com.vinicius.ecommerce.exceptions;

import com.vinicius.ecommerce.exceptions.reponse.ApiException;
import org.springframework.http.HttpStatus;

public class CategoryNotFoudException extends ApiException {

    public CategoryNotFoudException() {
        super("Category not found!", HttpStatus.NOT_FOUND);
    }
}
