package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.InventoryTransaction;
import com.yab.multitenantERP.enums.InventoryTransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {

    @Query("""
        SELECT t FROM InventoryTransaction t
        WHERE (:reference IS NULL OR t.reference LIKE %:reference%)
        AND (:type IS NULL OR t.type = :type)
        AND (:warehouseId IS NULL OR t.warehouse.id = :warehouseId)
        AND (:posted IS NULL OR t.posted = :posted)
        AND (:startDate IS NULL OR t.date >= :startDate)
        AND (:endDate IS NULL OR t.date <= :endDate)
    """)
    Page<InventoryTransaction> searchTransactions(
        @Param("reference") String reference,
        @Param("type") InventoryTransactionType type,
        @Param("warehouseId") Long warehouseId,
        @Param("posted") Boolean posted,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable
    );
}
