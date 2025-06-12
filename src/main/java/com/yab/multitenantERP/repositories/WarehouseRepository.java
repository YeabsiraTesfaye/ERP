package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}
