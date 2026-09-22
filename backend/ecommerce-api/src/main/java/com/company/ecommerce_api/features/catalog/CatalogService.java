package com.company.ecommerce_api.features.catalog;

import com.company.ecommerce_api.features.product.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CatalogService {

    private final ProductRepository productRepository;

    public CatalogService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Page<ProductResponse> findActiveProducts(int page, int size, String query, Long categoryId) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);
        String normalizedQuery = query == null || query.isBlank() ? null : query.trim();

        return productRepository.searchActive(normalizedQuery, categoryId, PageRequest.of(safePage, safeSize))
                .map(ProductResponse::from);
    }

    @Transactional
    public ProductResponse findActiveProduct(Long id) {
        return productRepository.findById(id)
                .filter(product -> product.isActive())
                .map(ProductResponse::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                ));
    }
}