package com.company.ecommerce_api.features.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserAccountEmailIgnoreCaseOrderByCreatedAtDesc(String email);

    java.util.Optional<Order> findByIdAndUserAccountEmailIgnoreCase(Long id, String email);
}