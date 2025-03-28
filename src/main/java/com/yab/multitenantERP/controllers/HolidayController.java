package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.entity.Holiday;
import com.yab.multitenantERP.services.HolidayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/holiday")
public class HolidayController {
    private final HolidayService holidayService;
    private static final Logger logger = LoggerFactory.getLogger(HolidayService.class);

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }
    @PutMapping("/{holidayId}")
    public ResponseEntity<Holiday> updateHoliday(
            @PathVariable Long holidayId,
            @RequestBody Holiday updatedHoliday) {
        Holiday holiday = holidayService.updateHoliday(holidayId, updatedHoliday);
        return ResponseEntity.ok(holiday);
    }

}
