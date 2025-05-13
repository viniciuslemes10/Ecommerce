package com.vinicius.ecommerce.records.data;

import com.vinicius.ecommerce.model.Category;

public record CategoryDetailsDTO(
        Long id,
        String name,
        String description
) {

    public CategoryDetailsDTO(Category category) {
        this(category.getId(), category.getName(), category.getDescription());
    }
}
