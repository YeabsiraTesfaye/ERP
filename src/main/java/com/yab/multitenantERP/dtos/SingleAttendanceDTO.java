package com.yab.multitenantERP.dtos;

import com.yab.multitenantERP.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SingleAttendanceDTO {
    private int day;
    private int month;
    private int year;
    private String dayName;
    private AttendanceStatus status;
}