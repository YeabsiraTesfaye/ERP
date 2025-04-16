package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.EmployeeAllowance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeAllowanceRepository extends JpaRepository<EmployeeAllowance, Long> {
    List<EmployeeAllowance> findByEmployeeId(Long employeeId);
    List<EmployeeAllowance> findByAllowanceId(Long allowanceId);
}
