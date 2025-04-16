package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.EmployeePayroll;
import com.yab.multitenantERP.entity.PayrollDateRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeePayrollRepository extends JpaRepository<EmployeePayroll, Long> {
    Optional<EmployeePayroll> findByEmployee_IdAndPayroll_DateRange(Long employeeId, PayrollDateRange dateRange);

}
