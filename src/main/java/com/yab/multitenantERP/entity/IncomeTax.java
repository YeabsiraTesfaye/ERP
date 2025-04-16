package com.yab.multitenantERP.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "income_tax")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeTax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double minimum;

    private Double maximum;

    @Column(nullable = false)
    private Double rate;

    @Column(nullable = false)
    private Double deduction;

    // Tax level indicator (e.g., LOW, MEDIUM, HIGH)
    @Column(nullable = false, unique = true)
    private String level;
}
