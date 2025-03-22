package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Branch;
import com.yab.multitenantERP.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
