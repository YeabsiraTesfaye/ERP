package com.yab.multitenantERP.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    Employee employee;
}
