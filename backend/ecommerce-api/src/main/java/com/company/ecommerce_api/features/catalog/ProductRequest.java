package com.company.ecommerce_api.features.catalog;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank String name,
        @NotBlank String slug,
        String description,
        @DecimalMin(value = "0.00") BigDecimal price,
        @Min(0) int stock,
        Long categoryId
) {
}