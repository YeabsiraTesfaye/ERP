package com.yab.multitenantERP.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JournalEntryLineDTO {
    private String accountCode;
    private BigDecimal debit;
    private BigDecimal credit;
}


