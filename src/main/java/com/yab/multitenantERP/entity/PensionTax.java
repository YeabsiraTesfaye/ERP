package com.yab.multitenantERP.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "pension_tax")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PensionTax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Percentage rate for the employee's contribution (e.g., 0.05 for 5%)
    @Column(nullable = false)
    private BigDecimal employeeContributionRate;

    // Percentage rate for the company's contribution (e.g., 0.05 for 5%)
    @Column(nullable = false)
    private BigDecimal companyContributionRate;
}
