package com.company.ecommerce_api.features.profile;

public record ProfileResponse(
        String email,
        String firstName,
        String lastName,
        String role
) {
}