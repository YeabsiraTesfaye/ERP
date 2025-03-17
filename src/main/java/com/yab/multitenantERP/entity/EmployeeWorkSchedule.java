package com.yab.multitenantERP.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.List;

@Entity
@Getter
@Setter
public class EmployeeWorkSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> workingDays;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> weekends;

    // Getters and Setters
}
