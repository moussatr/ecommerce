package com.company.ecommerce_api.features.catalog;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank String name,
        @NotBlank String slug
) {
}