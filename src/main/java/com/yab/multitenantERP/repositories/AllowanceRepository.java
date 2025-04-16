package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Address;
import com.yab.multitenantERP.entity.Allowance;
import com.yab.multitenantERP.entity.Role;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface AllowanceRepository extends JpaRepository<Allowance,Long> {

    @Query(value = """
    SELECT b.* FROM allowances b 
    JOIN employee_allowance eb ON b.allowance_id = eb.allowance_id 
    WHERE eb.employee_id = :employeeId
""", nativeQuery = true)
    List<Allowance> findByEmployeeId(@Param("employeeId") Long employeeId);


    @Query(value = """
    SELECT b.* FROM allowances b 
    JOIN position_allowance eb ON b.allowance_id = eb.allowance_id 
    WHERE eb.position_id = :positionId
""", nativeQuery = true)
    List<Allowance> findByPositionId(@Param("positionId") Long positionId);
}
