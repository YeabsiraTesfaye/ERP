package com.yab.multitenantERP.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AllowanceBreakdownDTO {
    private String allowanceName;
    private BigDecimal totalAmount;
    private BigDecimal taxableAmount;
}
