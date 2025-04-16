package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.dtos.AttendanceResponse;
import com.yab.multitenantERP.dtos.EmployeeAttendanceRequest;
import com.yab.multitenantERP.entity.Attendance;
import com.yab.multitenantERP.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class EmployeeAttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/fill")
    public ResponseEntity<Attendance> markAttendance(@RequestBody EmployeeAttendanceRequest request) {
        Attendance attendance = attendanceService.markAttendance(
                request.getEmployeeId(),
                request.getSession(),
                request.getStatus()
        );
        return ResponseEntity.ok(attendance);
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> submitMultipleAttendances(@RequestBody List<EmployeeAttendanceRequest> request) {
        for (EmployeeAttendanceRequest ar : request) {
            attendanceService.markAttendance(
                    ar.getEmployeeId(),
                    ar.getSession(),
                    ar.getStatus()
            ); // Or call your save logic directly
        }
        return ResponseEntity.ok("Attendance submitted successfully.");
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Page<AttendanceResponse
            >> getByEmployee(
            @PathVariable Long employeeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "year,desc") String[] sort
    ) {
        Sort sortOrder = Sort.by(Arrays.stream(sort)
                .map(s -> {
                    String[] parts = s.split(",");
                    return new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]);
                }).toList());

        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<AttendanceResponse> result = attendanceService.findByEmployee(employeeId, start, end, pageable);
        return ResponseEntity.ok(result);
    }

}
