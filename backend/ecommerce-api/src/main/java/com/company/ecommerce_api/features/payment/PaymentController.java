package com.company.ecommerce_api.features.payment;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final StripeCheckoutService stripeCheckoutService;

    public PaymentController(StripeCheckoutService stripeCheckoutService) {
        this.stripeCheckoutService = stripeCheckoutService;
    }

    @PostMapping("/checkout")
    public CheckoutResponse createCheckout(
            Authentication authentication,
            @Valid @RequestBody CheckoutRequest request
    ) {
        return stripeCheckoutService.createCheckout(authentication, request.orderId());
    }

    @PostMapping(value = "/webhook", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Boolean>> handleWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "Stripe-Signature", required = false) String stripeSignature
    ) {
        stripeCheckoutService.handleCheckoutSessionCompleted(payload, stripeSignature);
        return ResponseEntity.ok(Map.of("received", true));
    }
}