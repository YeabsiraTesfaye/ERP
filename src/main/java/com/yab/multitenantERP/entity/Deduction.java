package com.yab.multitenantERP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yab.multitenantERP.enums.DeductedFromTypes;
import com.yab.multitenantERP.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "deductions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deduction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deduction_id")
    private Long id;

    @Column(name = "deduction_name", nullable = false)
    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private Double amount;

    private boolean isRate;

    private DeductedFromTypes deductFrom;

    @Builder.Default
    private boolean status = true;
}
