package com.company.ecommerce_api.features.profile;

import jakarta.validation.constraints.NotBlank;

public record ProfileRequest(
        @NotBlank String firstName,
        @NotBlank String lastName
) {
}