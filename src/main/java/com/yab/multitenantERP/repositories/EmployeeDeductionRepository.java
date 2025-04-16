package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Deduction;
import com.yab.multitenantERP.entity.EmployeeBenefit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeDeductionRepository extends JpaRepository<Deduction, Long> {
    List<Deduction> findByEmployeeId(Long employeeId);
}
