package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Department;
import com.yab.multitenantERP.entity.DepartmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentHistoryRepository extends JpaRepository<DepartmentHistory, Long> {
}
