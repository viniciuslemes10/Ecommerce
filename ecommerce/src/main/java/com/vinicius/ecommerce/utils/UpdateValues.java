package com.vinicius.ecommerce.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.function.Consumer;

@Component
public class UpdateValues {

    public static void updateIfNotNullOrEmpty(String value, Consumer<String> setter) {
        if(value != null && !value.isBlank()) {
            setter.accept(value);
        }
    }

    public static void updateIfNotNullOrEmpty(BigDecimal value, Consumer<BigDecimal> setter) {
        if (value != null && value.compareTo(BigDecimal.ZERO) > 0) {
            setter.accept(value);
        }
    }

    public static void updateIfNotNullOrEmpty(Integer value, Consumer<Integer> setter) {
        if (value != null && value > 0) {
            setter.accept(value);
        }
    }
}
