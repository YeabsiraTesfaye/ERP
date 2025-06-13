package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.UnitOfMeasure;
import com.yab.multitenantERP.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitOfMeasurementRepository extends JpaRepository<UnitOfMeasure, Long> {
}
