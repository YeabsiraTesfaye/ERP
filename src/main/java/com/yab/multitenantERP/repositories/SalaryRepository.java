package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Role;
import com.yab.multitenantERP.entity.Salary;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {
    // Custom query to find Salary by Position
//    @Query("SELECT s FROM Salary s WHERE s.position.id = :positionId")
//    List<Salary> findByPositionId(@Param("positionId") Long positionId);

}
