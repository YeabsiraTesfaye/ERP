package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.PositionAllowance;
import com.yab.multitenantERP.entity.PositionBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionAllowanceRepository extends JpaRepository<PositionAllowance, Long> {

    List<PositionAllowance> findByPositionId(Long positionId);
    PositionAllowance findByAllowanceId(Long allowanceId);

}
