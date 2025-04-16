package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Payroll;
import com.yab.multitenantERP.entity.PayrollDateRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    Optional<Payroll> findByDateRange(PayrollDateRange dateRange);
}
