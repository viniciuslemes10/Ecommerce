package com.vinicius.ecommerce.records.data;

import com.vinicius.ecommerce.model.Product;

import java.math.BigDecimal;

public record ProductDetailsDTO(
        String name,
        String description,
        BigDecimal price,
        String image,
        Integer stock
) {
    public ProductDetailsDTO(Product productSave) {
        this(productSave.getName(), productSave.getDescription(), productSave.getPrice(),
                productSave.getImageUrl(), productSave.getStock());
    }
}
