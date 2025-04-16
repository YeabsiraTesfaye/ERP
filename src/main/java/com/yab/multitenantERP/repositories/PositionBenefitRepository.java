package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.EmployeeBenefit;
import com.yab.multitenantERP.entity.PositionBenefit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionBenefitRepository extends JpaRepository<PositionBenefit, Long> {
    List<PositionBenefit> findByPositionId(Long employeeId);
    PositionBenefit findByBenefitId(Long allowanceId);
}
