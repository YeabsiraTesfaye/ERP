package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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


}
