package com.company.ecommerce_api.features.catalog;

import com.company.ecommerce_api.features.product.Category;
import com.company.ecommerce_api.features.product.CategoryRepository;
import com.company.ecommerce_api.features.product.Product;
import com.company.ecommerce_api.features.product.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminCatalogService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public AdminCatalogService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = Product.create(
                request.name(), request.slug(), request.description(), request.price(),
                request.stock(), findCategory(request.categoryId())
        );
        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = findProduct(id);
        product.update(
                request.name(), request.slug(), request.description(), request.price(),
                request.stock(), findCategory(request.categoryId())
        );
        return ProductResponse.from(product);
    }

    @Transactional
    public void deactivateProduct(Long id) {
        findProduct(id).deactivate();
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        return CategoryResponse.from(categoryRepository.save(
                Category.create(request.name(), request.slug())
        ));
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = findCategory(id);
        category.update(request.name(), request.slug());
        return CategoryResponse.from(category);
    }

    @Transactional
    public void deactivateCategory(Long id) {
        findCategory(id).deactivate();
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    private Category findCategory(Long id) {
        if (id == null) {
            return null;
        }
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }
}