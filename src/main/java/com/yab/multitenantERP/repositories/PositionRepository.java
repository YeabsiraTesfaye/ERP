package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Department;
import com.yab.multitenantERP.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
}
