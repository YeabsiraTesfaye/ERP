package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Employee;
import com.yab.multitenantERP.entity.Product;
import com.yab.multitenantERP.entity.UnitOfMeasure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
       SELECT p FROM Product p
       WHERE (:name IS NULL OR TRIM(:name) = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
       AND (:sku IS NULL OR TRIM(:sku) = '' OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :sku, '%')))
       """)
    Page<Product> getProducts(
            @Param("name") String name,
            @Param("sku") String sku,
            Pageable pageable);

}
