package com.yab.multitenantERP.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceResponse {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private int year;
    private int month;
    private int day;
    private String dayName;
    private String session;
    private String status;
}
