package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.AttendancePolicy;
import com.yab.multitenantERP.enums.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendancePolicyRepository extends JpaRepository<AttendancePolicy, Long> {
    Optional<AttendancePolicy> findBySession(AttendanceSession session);
}
