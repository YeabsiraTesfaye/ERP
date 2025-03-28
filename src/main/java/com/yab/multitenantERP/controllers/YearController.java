package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.dtos.HolidayRequestDTO;
import com.yab.multitenantERP.entity.Holiday;
import com.yab.multitenantERP.entity.Year;
import com.yab.multitenantERP.services.HolidayService;
import com.yab.multitenantERP.services.YearService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/year")
class YearController {
    private final YearService yearService;
    private final HolidayService holidayService;
    YearController(YearService yearService, HolidayService holidayService) {
        this.yearService = yearService;
        this.holidayService = holidayService;
    }
    @PostMapping
    public ResponseEntity<Year> createYear(@RequestBody Year year) {
        Year savedYear = yearService.saveYear(year);
        return ResponseEntity.ok(savedYear);
    }
    @GetMapping
    public List<Year> getAllYears() {
        return yearService.getAllYears();
    }

    @GetMapping("/{id}")
    public Optional<Year> getAllYears(@PathVariable Long id) {
        return yearService.getYear(id);
    }
    @PutMapping("/{id}")
    public Year updateYear(@PathVariable Long id, @RequestBody Year year) {
        return yearService.updateYear(id, year);
    }
    @PostMapping("/addHolidaysInYear")
    public ResponseEntity<List<Holiday>> addHolidays(@RequestBody HolidayRequestDTO request) {
        List<Holiday> savedHolidays = holidayService.addHolidayToYear(request);
        return ResponseEntity.ok(savedHolidays);
    }


    @GetMapping("/getHolidaysByYearId/{yearId}/holidays")
    public ResponseEntity<List<Holiday>> getHolidaysByYear(@PathVariable Long yearId) {
        List<Holiday> holidays = holidayService.getHolidaysByYear(yearId);
        return ResponseEntity.ok(holidays);
    }
}