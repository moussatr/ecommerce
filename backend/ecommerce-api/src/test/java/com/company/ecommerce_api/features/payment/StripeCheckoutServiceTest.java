package com.company.ecommerce_api.features.payment;

import com.company.ecommerce_api.features.order.Order;
import com.company.ecommerce_api.features.order.OrderRepository;
import com.company.ecommerce_api.features.order.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class StripeCheckoutServiceTest {

    @Test
    void handleCheckoutSessionCompleted_whenSignatureIsValid_marksOrderPaid() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        Order order = mock(Order.class);
        when(orderRepository.findById(42L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        StripeCheckoutService service = new StripeCheckoutService(
                orderRepository,
                "sk_test_123",
                "whsec_test_secret",
                "http://localhost:4200/orders",
                "http://localhost:4200/cart"
        );

        String payload = "{\"id\":\"evt_123\",\"type\":\"checkout.session.completed\",\"data\":{\"object\":{\"id\":\"cs_123\",\"metadata\":{\"order_id\":\"42\"}}}}";
        String signature = buildStripeSignature("whsec_test_secret", payload);

        service.handleCheckoutSessionCompleted(payload, signature);

        verify(order).markPaid();
        verify(orderRepository).save(order);
    }

    @Test
    void handleCheckoutSessionCompleted_whenSignatureIsInvalid_throwsBadRequest() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        StripeCheckoutService service = new StripeCheckoutService(
                orderRepository,
                "sk_test_123",
                "whsec_test_secret",
                "http://localhost:4200/orders",
                "http://localhost:4200/cart"
        );

        String payload = "{\"id\":\"evt_123\",\"type\":\"checkout.session.completed\",\"data\":{\"object\":{\"id\":\"cs_123\",\"metadata\":{\"order_id\":\"42\"}}}}";

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.handleCheckoutSessionCompleted(payload, "t=123,v1=invalid")
        );

        assertEquals(400, exception.getStatusCode().value());
        verify(orderRepository, never()).findById(anyLong());
    }

    private String buildStripeSignature(String secret, String payload) {
        long timestamp = System.currentTimeMillis() / 1000L;
        String signedPayload = timestamp + "." + payload;
        String signature = hmacSha256(secret, signedPayload);
        return "t=" + timestamp + ",v1=" + signature;
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
            throw new RuntimeException(exception);
        }
    }
}
