package com.company.ecommerce_api.features.catalog;

import com.company.ecommerce_api.features.product.Category;

public record CategoryResponse(Long id, String name, String slug) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getSlug());
    }
}