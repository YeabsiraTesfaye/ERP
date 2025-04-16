package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Attendance;
import com.yab.multitenantERP.enums.AttendanceSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByYearAndMonth(int year, int month);

    @Query("SELECT a FROM Attendance a WHERE a.year = :year AND a.month = :month AND a.day = :day")
    List<Attendance> findByExactDate(@Param("year") int year, @Param("month") int month, @Param("day") int day);

    @Query(value = "SELECT * FROM attendance a WHERE " +
            "TO_DATE(CONCAT(a.year, '-', LPAD(a.month::text, 2, '0'), '-', LPAD(a.day::text, 2, '0')), 'YYYY-MM-DD') " +
            "BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Attendance> findByDateRange(@Param("startDate") LocalDate start, @Param("endDate") LocalDate end);

    boolean existsByEmployeeIdAndYearAndMonthAndDayAndSession(
            Long employeeId, int year, int month, int day, AttendanceSession session);

    @Query(value = "SELECT * FROM attendance a WHERE a.employee_id = :employeeId",
            countQuery = "SELECT COUNT(*) FROM attendance a WHERE a.employee_id = :employeeId",
            nativeQuery = true)
    Page<Attendance> findByEmployeeId(@Param("employeeId") Long employeeId, Pageable pageable);

    @Query(value = "SELECT * FROM attendance a WHERE a.employee_id = :employeeId AND " +
            "TO_DATE(CONCAT(a.year, '-', LPAD(a.month::text, 2, '0'), '-', LPAD(a.day::text, 2, '0')), 'YYYY-MM-DD') " +
            "BETWEEN :startDate AND :endDate",
            countQuery = "SELECT COUNT(*) FROM attendance a WHERE a.employee_id = :employeeId AND " +
                    "TO_DATE(CONCAT(a.year, '-', LPAD(a.month::text, 2, '0'), '-', LPAD(a.day::text, 2, '0')), 'YYYY-MM-DD') " +
                    "BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    Page<Attendance> findByEmployeeIdAndDateRange(@Param("employeeId") Long employeeId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate,
                                                  Pageable pageable);

}
