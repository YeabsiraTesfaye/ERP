package com.yab.multitenantERP.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PositionSaveDTO {

    private String positionName;
    private Integer requiredManPower;
    private Long departmentId;  // Assuming the department ID is passed
    private Double salaryAmount;  // The salary to be set for this position
    private LocalDate salaryEffectiveDate;  // The effective date of the salary

    // Optionally, you can include employee ID if needed
    private Long employeeId;  // If you want to associate a specific employee with the position

}
