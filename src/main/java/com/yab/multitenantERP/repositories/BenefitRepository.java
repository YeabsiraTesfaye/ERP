package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Address;
import com.yab.multitenantERP.entity.Benefit;
import com.yab.multitenantERP.entity.Role;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface BenefitRepository extends JpaRepository<Benefit,Long> {

    @Query(value = """
    SELECT b.* FROM benefits b 
    JOIN employee_benefit eb ON b.benefit_id = eb.benefit_id 
    WHERE eb.employee_id = :employeeId
""", nativeQuery = true)
    List<Benefit> findByEmployeeId(@Param("employeeId") Long employeeId);


    @Query(value = """
    SELECT b.* FROM benefits b 
    JOIN position_benefit eb ON b.benefit_id = eb.benefit_id 
    WHERE eb.position_id = :positionId
""", nativeQuery = true)
    List<Benefit> findByPositionId(@Param("positionId") Long positionId);
}
