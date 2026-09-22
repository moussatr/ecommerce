package com.company.ecommerce_api.features.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

        @Query("""
            select p from Product p
            where p.active = true
                  and (coalesce(:query, '') = ''
                    or lower(p.name) like lower(concat('%', coalesce(:query, ''), '%'))
                    or lower(p.slug) like lower(concat('%', coalesce(:query, ''), '%')))
              and (:categoryId is null or p.category.id = :categoryId)
            order by p.createdAt desc
            """)
        Page<Product> searchActive(
            @Param("query") String query,
            @Param("categoryId") Long categoryId,
            Pageable pageable
        );
}