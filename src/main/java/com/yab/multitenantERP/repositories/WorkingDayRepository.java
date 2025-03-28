package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.WorkingDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkingDayRepository extends JpaRepository<WorkingDay, Long> {
}
