package com.company.ecommerce_api.features.payment;

import com.company.ecommerce_api.features.order.Order;
import com.company.ecommerce_api.features.order.OrderItem;
import com.company.ecommerce_api.features.order.OrderRepository;
import com.company.ecommerce_api.features.order.OrderStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeCheckoutService {

    private final OrderRepository orderRepository;
    private final RestClient restClient;
    private final String secretKey;
    private final String webhookSecret;
    private final String successUrl;
    private final String cancelUrl;

    public StripeCheckoutService(
            OrderRepository orderRepository,
            @Value("${stripe.secret-key:}") String secretKey,
            @Value("${stripe.webhook-secret:}") String webhookSecret,
            @Value("${stripe.success-url:http://localhost:4200/orders}") String successUrl,
            @Value("${stripe.cancel-url:http://localhost:4200/cart}") String cancelUrl
    ) {
        this.orderRepository = orderRepository;
        this.restClient = RestClient.create();
        this.secretKey = secretKey;
        this.webhookSecret = webhookSecret;
        this.successUrl = successUrl;
        this.cancelUrl = cancelUrl;
    }

    @Transactional
    public CheckoutResponse createCheckout(Authentication authentication, Long orderId) {
        if (secretKey.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Stripe is not configured"
            );
        }

        Order order = orderRepository.findByIdAndUserAccountEmailIgnoreCase(orderId, authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (order.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order has no items");
        }

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("mode", "payment");
        form.add("success_url", successUrl);
        form.add("cancel_url", cancelUrl);
        form.add("metadata[order_id]", order.getId().toString());

        int index = 0;
        for (OrderItem item : order.getItems()) {
            String prefix = "line_items[" + index + "]";
            form.add(prefix + "[quantity]", Integer.toString(item.getQuantity()));
            form.add(prefix + "[price_data][currency]", "eur");
            form.add(prefix + "[price_data][unit_amount]", toCents(item.getUnitPrice()).toString());
            form.add(prefix + "[price_data][product_data][name]", item.getProductName());
            index++;
        }

        try {
            var response = restClient.post()
                    .uri("https://api.stripe.com/v1/checkout/sessions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(com.fasterxml.jackson.databind.JsonNode.class);

            if (response == null || response.get("id") == null || response.get("url") == null) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Invalid Stripe response");
            }
            return new CheckoutResponse(response.get("id").asText(), response.get("url").asText());
        } catch (ResponseStatusException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Stripe checkout failed", exception);
        }
    }

    @Transactional
    public void handleCheckoutSessionCompleted(String payload, String stripeSignature) {
        if (webhookSecret.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Stripe webhook is not configured");
        }
        if (!hasValidSignature(payload, stripeSignature)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Stripe signature");
        }

        try {
            JsonNode event = new ObjectMapper().readTree(payload);
            if (!"checkout.session.completed".equals(event.path("type").asText())) {
                return;
            }

            JsonNode orderIdNode = event.path("data").path("object").path("metadata").path("order_id");
            if (orderIdNode.isMissingNode() || orderIdNode.asText().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing order id in Stripe event");
            }

            Long orderId = Long.parseLong(orderIdNode.asText());
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

            if (order.getStatus() == OrderStatus.PAID) {
                return;
            }

            order.markPaid();
            orderRepository.save(order);
        } catch (NumberFormatException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order id in Stripe event", exception);
        } catch (ResponseStatusException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Stripe event payload", exception);
        }
    }

    private boolean hasValidSignature(String payload, String stripeSignature) {
        if (payload == null || payload.isBlank() || stripeSignature == null || stripeSignature.isBlank()) {
            return false;
        }

        Map<String, String> headerValues = new HashMap<>();
        for (String part : stripeSignature.split(",")) {
            String[] pair = part.split("=", 2);
            if (pair.length == 2) {
                headerValues.put(pair[0].trim(), pair[1].trim());
            }
        }

        String timestamp = headerValues.get("t");
        String signature = headerValues.get("v1");
        if (timestamp == null || signature == null) {
            return false;
        }

        String signedPayload = timestamp + "." + payload;
        String expectedSignature = hmacSha256(webhookSecret, signedPayload);
        return MessageDigest.isEqual(
                expectedSignature.toLowerCase().getBytes(StandardCharsets.UTF_8),
                signature.toLowerCase().getBytes(StandardCharsets.UTF_8)
        );
    }

    private String hmacSha256(String secret, String payload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte value : hash) {
                hex.append(String.format("%02x", value));
            }
            return hex.toString();
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to verify Stripe signature", exception);
        }
    }

    private BigDecimal toCents(BigDecimal amount) {
        return amount.movePointRight(2).setScale(0);
    }
}