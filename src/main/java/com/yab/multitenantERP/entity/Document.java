package com.yab.multitenantERP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yab.multitenantERP.enums.EmployeeDocumentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NonNull
    String name;

    @NonNull
    String fileName;

    @NonNull
    EmployeeDocumentType employeeDocumentType;

    Long fileSize;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "employee_id")
    Employee employee;
}
