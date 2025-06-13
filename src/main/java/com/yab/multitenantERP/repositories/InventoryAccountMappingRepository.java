package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.InventoryAccountMapping;
import com.yab.multitenantERP.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InventoryAccountMappingRepository extends JpaRepository<InventoryAccountMapping, Long> {
    @Query("""
       SELECT p FROM InventoryAccountMapping p
       WHERE (:name IS NULL OR TRIM(:name) = '' OR LOWER(p.product.name) LIKE LOWER(CONCAT('%', :name, '%')))
       AND (:sku IS NULL OR TRIM(:sku) = '' OR LOWER(p.product.sku) LIKE LOWER(CONCAT('%', :sku, '%')))
       """)
    Page<InventoryAccountMapping> getMappings(
            @Param("name") String name,
            @Param("sku") String sku,
            Pageable pageable);
}
