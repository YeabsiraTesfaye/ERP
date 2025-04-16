package com.yab.multitenantERP.dtos;

import com.yab.multitenantERP.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PositionFetchDTO {
    Position position;
    List<PositionAllowance> positionAllowances;
    List<PositionBenefit> positionBenefits;
}
