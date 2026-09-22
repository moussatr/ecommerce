package com.company.ecommerce_api.features.cart;

import com.company.ecommerce_api.features.catalog.ProductResponse;

public record CartItemResponse(ProductResponse product, int quantity) {

    public static CartItemResponse from(CartItem item) {
        return new CartItemResponse(ProductResponse.from(item.getProduct()), item.getQuantity());
    }
}