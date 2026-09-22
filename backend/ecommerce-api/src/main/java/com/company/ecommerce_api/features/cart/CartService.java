package com.company.ecommerce_api.features.cart;

import com.company.ecommerce_api.features.product.Product;
import com.company.ecommerce_api.features.product.ProductRepository;
import com.company.ecommerce_api.shared.security.UserAccount;
import com.company.ecommerce_api.shared.security.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserAccountRepository userAccountRepository;

    public CartService(
            CartRepository cartRepository,
            ProductRepository productRepository,
            UserAccountRepository userAccountRepository
    ) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional
    public CartResponse getCart(Authentication authentication) {
        return CartResponse.from(findOrCreateCart(authentication));
    }

    @Transactional
    public CartResponse addItem(Authentication authentication, CartItemRequest request) {
        Cart cart = findOrCreateCart(authentication);
        Product product = findAvailableProduct(request.productId());
        CartItem item = findItem(cart, product.getId());
        int quantity = item == null ? request.quantity() : item.getQuantity() + request.quantity();
        validateQuantity(product, quantity);

        if (item == null) {
            cart.addItem(CartItem.create(product, request.quantity()));
        } else {
            item.setQuantity(quantity);
        }
        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse updateItem(
            Authentication authentication,
            Long productId,
            CartItemRequest request
    ) {
        Cart cart = findOrCreateCart(authentication);
        Product product = findAvailableProduct(productId);
        CartItem item = findItem(cart, productId);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found");
        }
        validateQuantity(product, request.quantity());
        item.setQuantity(request.quantity());
        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse removeItem(Authentication authentication, Long productId) {
        Cart cart = findOrCreateCart(authentication);
        CartItem item = findItem(cart, productId);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found");
        }
        cart.removeItem(item);
        return CartResponse.from(cart);
    }

    private Cart findOrCreateCart(Authentication authentication) {
        return cartRepository.findByUserAccountEmailIgnoreCase(authentication.getName())
                .orElseGet(() -> cartRepository.save(Cart.create(
                        userAccountRepository.findByEmailIgnoreCase(authentication.getName())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED))
                )));
    }

    private Product findAvailableProduct(Long productId) {
        return productRepository.findById(productId)
                .filter(Product::isActive)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    private CartItem findItem(Cart cart, Long productId) {
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private void validateQuantity(Product product, int quantity) {
        if (quantity > product.getStock()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Requested quantity exceeds stock");
        }
    }
}