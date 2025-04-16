package com.yab.multitenantERP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "salaries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_id")
    private Long id;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)  // Set to LAZY
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)  // Set to LAZY
    @JoinColumn(name = "position_id")
    private Position position;
}
