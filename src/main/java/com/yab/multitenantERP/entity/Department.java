package com.yab.multitenantERP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long id;

    @Column(name = "department_name", nullable = false)
    private String name;

    @Column(name = "required_manpower")
    private Integer requiredManPower;

    @JsonIgnore
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Employee> employees;

    @Builder.Default
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate = LocalDate.now();
}
