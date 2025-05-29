package com.yab.multitenantERP.services;

import com.yab.multitenantERP.dtos.*;
import com.yab.multitenantERP.entity.*;
import com.yab.multitenantERP.enums.AttendanceSession;
import com.yab.multitenantERP.enums.AttendanceStatus;
import com.yab.multitenantERP.enums.Status;
import com.yab.multitenantERP.repositories.AllowedAttendanceSessionRepository;
import com.yab.multitenantERP.repositories.AttendancePolicyRepository;
import com.yab.multitenantERP.repositories.AttendanceRepository;
import com.yab.multitenantERP.repositories.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    AttendanceRepository attendanceRepository;
    EmployeeRepository employeeRepository;
    AllowedAttendanceSessionRepository allowedAttendanceSessionRepository;
    AttendancePolicyRepository attendancePolicyRepository;
    AttendanceService(AttendanceRepository attendanceRepository,
                      EmployeeRepository employeeRepository,
                      AllowedAttendanceSessionRepository allowedAttendanceSessionRepository,
                      AttendancePolicyRepository attendancePolicyRepository){
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
        this.allowedAttendanceSessionRepository = allowedAttendanceSessionRepository;
        this.attendancePolicyRepository = attendancePolicyRepository;
    }
    public List<EmployeeAttendanceDTO> getAttendanceByYearMonth(int year, int month) {
        List<Attendance> attendances = attendanceRepository.findByYearAndMonth(year, month);
        return mapToDTO(attendances);
    }

    public List<EmployeeAttendanceDTO> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendances = attendanceRepository.findByDateRange(startDate, endDate);
        return mapToDTO(attendances);
    }

    private List<EmployeeAttendanceDTO> mapToDTO(List<Attendance> attendances) {
        return attendances.stream()
                .collect(Collectors.groupingBy(a -> a.getEmployee().getId()))
                .entrySet()
                .stream()
                .map(entry -> {
                    Long employeeId = entry.getKey();
                    List<Attendance> empAttendances = entry.getValue();
                    String name = empAttendances.get(0).getEmployee().getFirstName()+" "+empAttendances.get(0).getEmployee().getMiddleName()+" "+empAttendances.get(0).getEmployee().getLastName();

                    List<SingleAttendanceDTO> attendanceDTOs = empAttendances.stream()
                            .map(a -> new SingleAttendanceDTO(
                                    a.getDay(),
                                    a.getMonth(),
                                    a.getYear(),
                                    LocalDate.of(a.getYear(), a.getMonth(), a.getDay()).getDayOfWeek().name(),
                                    a.getStatus()
                            )).collect(Collectors.toList());

                    return new EmployeeAttendanceDTO(employeeId, name, attendanceDTOs);
                }).collect(Collectors.toList());
    }


    public Attendance markAttendance(Long employeeId, AttendanceSession session) {
        LocalDateTime now = LocalDateTime.now();

        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();

        // Check if attendance for this session already exists
        boolean alreadyFilled = attendanceRepository.existsByEmployeeIdAndYearAndMonthAndDayAndSession(
                employeeId, year, month, day, session
        );

        if (alreadyFilled) {
            throw new RuntimeException("Attendance already filled for this session.");
        }

        // Fetch the employee to get their shift
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Shift shift = employee.getShift(); // Get the employee's shift

        // Find the attendance policy for the employee's shift and the given session
        AttendancePolicy policy = shift.getAttendancePolicies().stream()
                .filter(p -> p.getSession().equals(session))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No attendance policy found for this session on the employee's shift"));

        // Set attendance status based on the policy's "late after" time
        AttendanceStatus attendanceStatus = AttendanceStatus.PRESENT;
        if (LocalTime.now().isAfter(policy.getLateAfter())) {
            attendanceStatus = AttendanceStatus.LATE;
        }

        // Generate day name
        LocalDate date = LocalDate.of(year, month, day); // Replace with your date
        String dayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        // Create and save the attendance record
        Attendance attendance = Attendance.builder()
                .employee(employee)
                .year(year)
                .month(month)
                .day(day)
                .dayName(dayName)
                .hour(now.getHour())
                .minute(now.getMinute())
                .second(now.getSecond())
                .session(session)
                .status(attendanceStatus)
                .build();

        return attendanceRepository.save(attendance);
    }


    public Page<AttendanceResponse> findByEmployee(Long employeeId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<Attendance> attendances;

        if (startDate != null && endDate != null) {
            attendances = attendanceRepository.findByEmployeeIdAndDateRange(employeeId, startDate, endDate, pageable);
        } else {
            attendances = attendanceRepository.findByEmployeeId(employeeId, pageable);
        }

        return attendances.map(this::mapToResponse);
    }
    private AttendanceResponse mapToResponse(Attendance a) {
        return AttendanceResponse.builder()
                .id(a.getId())
                .employeeId(a.getEmployee().getId())
                .employeeName(a.getEmployee().getFirstName()+" "+a.getEmployee().getMiddleName()+" "+a.getEmployee().getLastName())
                .year(a.getYear())
                .month(a.getMonth())
                .day(a.getDay())
                .dayName(a.getDayName())
                .session(a.getSession().name())
                .status(a.getStatus().name())
                .build();
    }


    public List<AttendanceSession> getAllowedSessions() {
        return allowedAttendanceSessionRepository.findByAllowedTrue().stream()
                .map(AllowedAttendanceSession::getSession)
                .collect(Collectors.toList());
    }

    public void setAllowedSessions(List<AttendanceSession> sessions) {
        allowedAttendanceSessionRepository.deleteAll(); // Clear previous
        List<AllowedAttendanceSession> newSessions = sessions.stream()
                .map(s -> new AllowedAttendanceSession(null, s, true))
                .collect(Collectors.toList());
        allowedAttendanceSessionRepository.saveAll(newSessions);
    }

    public List<String> saveBulk(List<BulkAttendance> bulkAttendances) {
        List<Attendance> attendances = new ArrayList<>();
        Set<String> duplicateEmployeeNames = new HashSet<>(); // Set to hold unique names of employees who already submitted attendance

        for (BulkAttendance ba : bulkAttendances) {
            LocalDate date = LocalDate.of(ba.getYear(), ba.getMonth(), ba.getDay());
            EmployeeAttendanceRequest employeeAttendanceRequest = new EmployeeAttendanceRequest();
            employeeAttendanceRequest.setEmployeeId(ba.getEmployeeId());
            employeeAttendanceRequest.setSession(ba.getSession());

            AttendanceStatus at = AttendanceStatus.PRESENT;
            Optional<Employee> employee = employeeRepository.findById(ba.getEmployeeId());

            if (employee.isPresent()) {
                if (employee.get().getStatus() == Status.DAY_OFF) {
                    at = AttendanceStatus.DAY_OFF;
                } else if (employee.get().getStatus() == Status.MATERNITY_LEAVE) {
                    at = AttendanceStatus.MATERNITY_LEAVE;
                } else if (employee.get().getStatus() == Status.PATERNITY_LEAVE) {
                    at = AttendanceStatus.PATERNITY_LEAVE;
                } else if (employee.get().getStatus() == Status.PAID_LEAVE) {
                    at = AttendanceStatus.PAID_LEAVE;
                } else if (employee.get().getStatus() == Status.UNPAID_LEAVE) {
                    at = AttendanceStatus.UNPAID_LEAVE;
                } else if (employee.get().getStatus() == Status.VACATION_LEAVE) {
                    at = AttendanceStatus.VACATION_LEAVE;
                } else {
                    Optional<AttendancePolicy> s = attendancePolicyRepository.findBySession(ba.getSession());
                    if (s.isPresent()) {
                        LocalTime time = LocalTime.of(ba.getHour(), ba.getMinute(), ba.getSecond());
                        if (time.isAfter(s.get().getLateAfter())) {
                            at = AttendanceStatus.LATE;
                        }
                    }
                }

                Attendance attendance = Attendance.builder()
                        .employee(employee.get())
                        .year(date.getYear())
                        .month(date.getMonthValue())
                        .day(date.getDayOfMonth())
                        .dayName(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                        .hour(ba.getHour())
                        .minute(ba.getMinute())
                        .second(ba.getSecond())
                        .session(ba.getSession())
                        .status(at)
                        .build();

                boolean alreadyFilled = attendanceRepository.existsByEmployeeIdAndYearAndMonthAndDayAndSession(
                        ba.getEmployeeId(), ba.getYear(), ba.getMonth(), ba.getDay(), ba.getSession()
                );

                if (alreadyFilled) {
                    duplicateEmployeeNames.add(employee.get().getFirstName() + " " + employee.get().getLastName());
                } else {
                    attendances.add(attendance);
                }
            }
        }

        attendanceRepository.saveAll(attendances);

        return new ArrayList<>(duplicateEmployeeNames); // Return list of unique employee names who have already submitted attendance
    }

}
