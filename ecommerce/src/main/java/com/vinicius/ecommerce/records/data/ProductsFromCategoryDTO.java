package com.vinicius.ecommerce.records.data;

import com.vinicius.ecommerce.model.Product;

import java.math.BigDecimal;

public record ProductsFromCategoryDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String image,
        Integer stock
) {
    public ProductsFromCategoryDTO(Product product) {
        this(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(), product.getImageUrl(), product.getStock());
    }
}
