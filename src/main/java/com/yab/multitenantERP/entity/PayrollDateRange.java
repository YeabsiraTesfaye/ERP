package com.yab.multitenantERP.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "payroll_date_ranges")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollDateRange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;
}
