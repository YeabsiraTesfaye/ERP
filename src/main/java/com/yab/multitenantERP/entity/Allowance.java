package com.yab.multitenantERP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "allowances")
@Builder
public class Allowance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allowance_id")
    private Long id;

    private String allowanceType;

    private Boolean isTaxable;

    private Long taxableAmount;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder.Default
    private boolean isActive = true;

    @JsonIgnore
    @OneToMany(mappedBy = "allowance", cascade = CascadeType.ALL)
    private List<EmployeeAllowance> employeeAllowances;

}
