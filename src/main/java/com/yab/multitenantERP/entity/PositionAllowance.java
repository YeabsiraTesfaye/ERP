package com.yab.multitenantERP.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "position_allowances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PositionAllowance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private long taxableAmount;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne
    @JoinColumn(name = "allowance_id")
    private Allowance allowance;
}
