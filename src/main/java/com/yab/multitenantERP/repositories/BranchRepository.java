package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Branch;
import com.yab.multitenantERP.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
}
