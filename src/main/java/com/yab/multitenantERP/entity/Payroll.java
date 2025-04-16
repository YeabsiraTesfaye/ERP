package com.yab.multitenantERP.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "payrolls")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to the payroll period
    @OneToOne
    @JoinColumn(name = "payroll_date_range_id", nullable = false)
    private PayrollDateRange dateRange;

    @Column(nullable = false)
    private LocalDate generatedDate;

    @OneToMany(mappedBy = "payroll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeePayroll> employeePayrolls;
}
