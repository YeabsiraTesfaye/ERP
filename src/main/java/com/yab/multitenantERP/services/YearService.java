package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.Year;
import com.yab.multitenantERP.repositories.YearRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class YearService {

    private final YearRepository yearRepository;

    public YearService(YearRepository yearRepository) {
        this.yearRepository = yearRepository;
    }

    @Transactional
    public Year saveYear(Year year) {
        // If your Year entity's setHolidays method sets the back-reference, this is enough.
        return yearRepository.save(year);
    }

    public List<Year> getAllYears(){
        return yearRepository.findAll();
    }


    public Optional<Year> getYear(Long id){
        return yearRepository.findById(id);
    }

    public Year updateYear(Long id, Year year){
        yearRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Year not found"));
        year.setId(id);
        return yearRepository.save(year);
    }
}
