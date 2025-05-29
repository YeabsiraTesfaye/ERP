package com.yab.multitenantERP.dtos;

import com.yab.multitenantERP.enums.AttendanceSession;
import com.yab.multitenantERP.enums.AttendanceStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class BulkAttendance {
    private Long employeeId;
    private AttendanceSession session;
    private int hour;
    private int minute;
    private int second;
    private int year;
    private int month;
    private int day;
}
