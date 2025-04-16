package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Address;
import com.yab.multitenantERP.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    // Custom query to find Salary by Position
    @Query("SELECT a FROM Address a WHERE a.employee.id = :employeeId")
    List<Address> findByEmployeeId(@Param("employeeId") Long employeeId);

}
