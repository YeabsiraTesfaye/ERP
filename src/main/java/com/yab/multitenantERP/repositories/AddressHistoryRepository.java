package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Address;
import com.yab.multitenantERP.entity.AddressHistory;
import com.yab.multitenantERP.entity.DepartmentHistory;
import com.yab.multitenantERP.entity.Role;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface AddressHistoryRepository extends JpaRepository<AddressHistory, Long> {
    @Query("SELECT a FROM AddressHistory a WHERE a.employee.id = :employeeId ORDER BY id DESC")
    List<AddressHistory> findByEmployeeId(@Param("employeeId") Long employeeId);
}
