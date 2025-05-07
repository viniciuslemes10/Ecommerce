package com.vinicius.ecommerce.records.data;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record ProductDTO(
        String name,
        String description,
        BigDecimal price,
        MultipartFile image,
        Integer stock
) {
}
