package com.yab.multitenantERP.services;

import com.yab.multitenantERP.dtos.AttendanceResponse;
import com.yab.multitenantERP.dtos.EmployeeAttendanceDTO;
import com.yab.multitenantERP.dtos.SingleAttendanceDTO;
import com.yab.multitenantERP.entity.AllowedAttendanceSession;
import com.yab.multitenantERP.entity.Attendance;
import com.yab.multitenantERP.entity.AttendancePolicy;
import com.yab.multitenantERP.entity.Employee;
import com.yab.multitenantERP.enums.AttendanceSession;
import com.yab.multitenantERP.enums.AttendanceStatus;
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
import java.util.List;
import java.util.Locale;
import java.util.Optional;
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


    public Attendance markAttendance(Long employeeId, AttendanceSession session, AttendanceStatus status) {
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

        AttendanceStatus attendanceStatus = status;
        Optional<AttendancePolicy> s = attendancePolicyRepository.findBySession(session);
        if(s.isPresent()){
            if(LocalTime.now().isAfter(s.get().getLateAfter())){
                attendanceStatus = AttendanceStatus.LATE;
            }
        }


        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        LocalDate date = LocalDate.of(year, month, day); // Replace with your date
        String dayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
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

}
