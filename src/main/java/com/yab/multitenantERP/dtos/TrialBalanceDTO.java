package com.yab.multitenantERP.dtos;

import com.yab.multitenantERP.enums.AccountType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class TrialBalanceDTO {
   String glCode;
   String name;
   AccountType type;
   BigDecimal debit;
   BigDecimal credit;
}
