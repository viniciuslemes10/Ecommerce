package com.vinicius.ecommerce.records.data;

import com.vinicius.ecommerce.model.Category;
import com.vinicius.ecommerce.model.Product;

import java.math.BigDecimal;

public record ProductDetailsDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String image,
        Integer stock,
        CategoryDetailsDTO category
) {

    public ProductDetailsDTO(Product product) {
        this(product.getId(), product.getName(), product.getDescription(), product.getPrice(),
                product.getImageUrl(), product.getStock(), new CategoryDetailsDTO(product.getCategory()));
    }
}
