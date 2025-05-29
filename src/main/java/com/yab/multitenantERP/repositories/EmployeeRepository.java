package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Employee;
import com.yab.multitenantERP.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("""
       SELECT e FROM Employee e 
       WHERE (:firstName IS NULL OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) 
       AND (:middleName IS NULL OR LOWER(e.middleName) LIKE LOWER(CONCAT('%', :middleName, '%'))) 
       AND (:lastName IS NULL OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')))
       """)
    Page<Employee> getFilteredEmployees(
            @Param("firstName") String firstName,
            @Param("middleName") String middleName,
            @Param("lastName") String lastName,
            Pageable pageable);

    List<Employee> findByStatusInAndDateOfHireLessThanEqual(List<Status> statuses, LocalDate hireDate);

    List<Employee> findBySupervisor_id(Long supervisorId);
    List<Employee> findByManager_id(Long supervisorId);
    List<Employee> findByBranch_id(Long branchId);
    List<Employee> findByShift_id(Long branchId);

    @Query("SELECT e FROM Employee e " +
            "WHERE e.position.level < :level " +
            "AND e.position.department.id = :departmentId")
    List<Employee> findEmployeesByDepartmentAndLowerPositionLevel(@Param("departmentId") Long departmentId,
                                                                   @Param("level") Long level);

}
