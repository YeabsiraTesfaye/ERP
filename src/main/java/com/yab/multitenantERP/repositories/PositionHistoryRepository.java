package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Position;
import com.yab.multitenantERP.entity.PositionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionHistoryRepository extends JpaRepository<PositionHistory, Long> {
}
