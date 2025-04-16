package com.yab.multitenantERP.dtos;

import com.yab.multitenantERP.entity.Deduction;
import com.yab.multitenantERP.entity.EmployeeAllowance;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeePayrollDTO {
    private Long id;
    private String fullName;
    private Double basicSalary;
    private Long employeeId;
    private Double overtime;
    private Double totalEarning;
    private Double totalDeduction;
    private Double taxableIncome;
    private List<Deduction> payrollOtherDeductionPerEmployee;
    private Double incomeTax;
    private Double netSalary;
    private Double pensionTaxFromEmployee;
    private Double pensionTaxFromEmployer;
    private LocalDate fromDate;
    private LocalDate toDate;
    private List<AllowanceBreakdownDTO> allowances;
    private Long preparedById;
    private LocalDate preparedDate;
    private LocalDate authorizedDate;
    private Long checkedById;
}
