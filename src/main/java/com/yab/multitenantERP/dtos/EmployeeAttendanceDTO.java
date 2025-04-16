package com.yab.multitenantERP.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EmployeeAttendanceDTO {
    private Long employeeId;
    private String employeeName;
    private List<SingleAttendanceDTO> attendance;
}


