package com.yab.multitenantERP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "benefits")
@Builder
public class Benefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "benefit_id")
    private Long id;

    @NonNull
    private String benefitType;  // E.g., Health Insurance, Retirement Plan, etc.

    @NonNull
    private String description;  // Description of the benefit

    private LocalDate startDate;  // When the benefit starts

    private LocalDate endDate;    // When the benefit ends (if applicable)

//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "employee_id")
//    private Employee employee;  // Employee who is receiving this benefit

    @JsonIgnore
@ManyToMany(fetch = FetchType.EAGER)
@JoinTable(name = "employee_benefit",
        joinColumns = @JoinColumn(name = "benefit_id"),
        inverseJoinColumns = @JoinColumn(name = "employee_id"))
private Set<Employee> employees;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "position_benefit",
            joinColumns = @JoinColumn(name = "benefit_id"),
            inverseJoinColumns = @JoinColumn(name = "position_id"))
    private Set<Position> positions;

    @Builder.Default
    private boolean isActive = true;  // Whether the benefit is currently active
}
