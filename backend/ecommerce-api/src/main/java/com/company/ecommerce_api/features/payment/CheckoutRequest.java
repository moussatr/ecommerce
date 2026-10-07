package com.company.ecommerce_api.features.payment;

import jakarta.validation.constraints.NotNull;

public record CheckoutRequest(@NotNull Long orderId) {
}