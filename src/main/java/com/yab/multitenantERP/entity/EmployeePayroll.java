package com.yab.multitenantERP.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "employee_payrolls")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeePayroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payroll_id", nullable = false)
    private Payroll payroll;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private BigDecimal basicSalary;

    @Column(nullable = false)
    private BigDecimal totalAllowances;

    @Column(nullable = false)
    private BigDecimal incomeTax;

    @Column(nullable = false)
    private BigDecimal pension;

    @Column(nullable = false)
    private BigDecimal deductions;

    @Column(nullable = false)
    private BigDecimal netSalary;

    @ManyToOne
    @JoinColumn(name = "payroll_date_range_id")
    private PayrollDateRange payrollDateRange;

    // You can add additional fields such as overtime, bonuses, etc. as needed.
}
