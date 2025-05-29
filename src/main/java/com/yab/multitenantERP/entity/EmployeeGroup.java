package com.yab.multitenantERP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "employee_groups")
public class EmployeeGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    String name;

    String description;

    @JsonIgnore
    @OneToMany(mappedBy = "employeeGroup", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("id DESC")
    List<Employee> employees;
}
