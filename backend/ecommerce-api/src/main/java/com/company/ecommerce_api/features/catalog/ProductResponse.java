package com.company.ecommerce_api.features.catalog;

import com.company.ecommerce_api.features.product.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String slug,
        String description,
        BigDecimal price,
        int stock,
        CategoryResponse category
) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory() == null ? null : CategoryResponse.from(product.getCategory())
        );
    }
}