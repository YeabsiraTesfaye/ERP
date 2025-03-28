package com.yab.multitenantERP.dtos;

import com.yab.multitenantERP.entity.Holiday;
import lombok.Data;

import java.util.List;

@Data
public class HolidayRequestDTO {
    private Long yearId;
    private List<Holiday> holidays;
}
