package com.company.ecommerce_api.features.order;

import com.company.ecommerce_api.features.cart.Cart;
import com.company.ecommerce_api.features.cart.CartItem;
import com.company.ecommerce_api.features.cart.CartRepository;
import com.company.ecommerce_api.shared.security.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserAccountRepository userAccountRepository;

    public OrderService(
            OrderRepository orderRepository,
            CartRepository cartRepository,
            UserAccountRepository userAccountRepository
    ) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional
    public OrderResponse createOrder(Authentication authentication) {
        Cart cart = cartRepository.findByUserAccountEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cart is empty");
        }

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new java.util.ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            if (!cartItem.getProduct().isActive() || cartItem.getQuantity() > cartItem.getProduct().getStock()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Product stock is no longer available");
            }
            cartItem.getProduct().decreaseStock(cartItem.getQuantity());
            total = total.add(cartItem.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItems.add(OrderItem.create(cartItem.getProduct(), cartItem.getQuantity()));
        }

        Order order = Order.create(
                userAccountRepository.findByEmailIgnoreCase(authentication.getName())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED)),
                total
        );
        for (OrderItem item : orderItems) {
            order.addItem(item);
        }
        Order savedOrder = orderRepository.save(order);
        cart.clear();
        return OrderResponse.from(savedOrder);
    }

    @Transactional
    public List<OrderResponse> findOrders(Authentication authentication) {
        return orderRepository.findByUserAccountEmailIgnoreCaseOrderByCreatedAtDesc(authentication.getName())
                .stream()
                .map(OrderResponse::from)
                .toList();
    }
}