package com.yab.multitenantERP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yab.multitenantERP.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "branches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private Long id;

    @Column(name = "branch_name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "status", nullable = false)
    private Status status;

    @JsonIgnore
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private List<Employee> employees;
}
