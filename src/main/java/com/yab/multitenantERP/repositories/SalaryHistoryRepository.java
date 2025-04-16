package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.PositionHistory;
import com.yab.multitenantERP.entity.SalaryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryHistoryRepository extends JpaRepository<SalaryHistory, Long> {
}
