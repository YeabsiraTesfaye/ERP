package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.AllowedAttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllowedAttendanceSessionRepository extends JpaRepository<AllowedAttendanceSession, Long> {
    List<AllowedAttendanceSession> findByAllowedTrue();
}
