package com.yab.multitenantERP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "positions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Long id;

    @Column(name = "position_name", nullable = false)
    private String name;

    @Column(name = "required_manpower")
    private Integer requiredManPower;

    @JsonIgnore
    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
    private List<Employee> employees;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;
}
