package com.vinicius.ecommerce.utils;

import com.vinicius.ecommerce.exceptions.InvalidProductException;
import com.vinicius.ecommerce.model.Product;

import java.math.BigDecimal;

public class ProductValidator {
    private ProductValidator() {
    }

    public static void validateProduct(Product product) {
        invalidName(product.getName());
        invalidDescription(product.getDescription());
        invalidPrice(product.getPrice());
        invalidStock(product.getStock());
    }

    private static void invalidStock(Integer stock) {
        if(stock == null || stock < 0) {
            throw new InvalidProductException("Product stock cannot be negative.");
        }
    }

    private static void invalidPrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductException("Product price must be greater than zero.");
        }
    }

    private static void invalidDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidProductException("Product description is required and cannot be empty.");
        }
    }

    private static void invalidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidProductException("Product name is required and cannot be empty.");
        }
    }
}
