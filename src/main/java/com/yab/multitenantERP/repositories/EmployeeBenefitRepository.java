package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.EmployeeAllowance;
import com.yab.multitenantERP.entity.EmployeeBenefit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeBenefitRepository extends JpaRepository<EmployeeBenefit, Long> {
    List<EmployeeBenefit> findByEmployeeId(Long employeeId);
    List<EmployeeBenefit> findByBenefitId(Long allowanceId);
}
