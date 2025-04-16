package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.PayrollDateRange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PayrollDateRangeRepository extends JpaRepository<PayrollDateRange, Long> {
    Optional<PayrollDateRange> findByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);
}