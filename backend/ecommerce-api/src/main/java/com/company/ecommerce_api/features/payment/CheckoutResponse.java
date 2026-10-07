package com.company.ecommerce_api.features.payment;

public record CheckoutResponse(String sessionId, String checkoutUrl) {
}