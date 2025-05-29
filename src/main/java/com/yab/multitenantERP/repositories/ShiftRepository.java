package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
}
