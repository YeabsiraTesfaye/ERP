package com.yab.multitenantERP.services;

import com.yab.multitenantERP.dtos.HolidayRequestDTO;
import com.yab.multitenantERP.entity.Holiday;
import com.yab.multitenantERP.entity.Year;
import com.yab.multitenantERP.repositories.HolidayRepository;
import com.yab.multitenantERP.repositories.YearRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class HolidayService {

    private final YearRepository yearRepository;
    private final HolidayRepository holidayRepository;

    public HolidayService(YearRepository yearRepository, HolidayRepository holidayRepository) {
        this.yearRepository = yearRepository;
        this.holidayRepository = holidayRepository;
    }

    @Transactional
    public List<Holiday> addHolidayToYear(HolidayRequestDTO request) {
        Year year = yearRepository.findById(request.getYearId())
                .orElseThrow(() -> new RuntimeException("Year not found with id " + request.getYearId()));

        for (Holiday holiday : request.getHolidays()) {
            holiday.setYear(year);
        }

        return holidayRepository.saveAll(request.getHolidays());
    }


    public List<Holiday> getHolidaysByYear(Long yearId) {
        return holidayRepository.findByYear_Id(yearId);
    }

    @Transactional
    public Holiday updateHoliday(Long holidayId, Holiday updatedHoliday) {
        Holiday holiday = holidayRepository.findById(holidayId)
                .orElseThrow(() -> new RuntimeException("Holiday not found with ID: " + holidayId));

        holiday.setName(updatedHoliday.getName());
        holiday.setDescription(updatedHoliday.getDescription());
        holiday.setMonth(updatedHoliday.getMonth());
        holiday.setDay(updatedHoliday.getDay());

        return holidayRepository.save(holiday);
    }

}
