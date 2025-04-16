package com.yab.multitenantERP.dtos;

import com.yab.multitenantERP.enums.AttendanceSession;
import com.yab.multitenantERP.enums.AttendanceStatus;
import lombok.Data;

@Data
public class EmployeeAttendanceRequest {
    private Long employeeId;
    private AttendanceSession session;
    private AttendanceStatus status;
}
