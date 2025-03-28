package com.yab.multitenantERP.services;

import com.yab.multitenantERP.config.CompanyContextHolder;
import com.yab.multitenantERP.entity.Role;
import com.yab.multitenantERP.entity.Tenant;
import com.yab.multitenantERP.entity.UserEntity;
import com.yab.multitenantERP.entity.WorkingDay;
import com.yab.multitenantERP.enums.Day;
import com.yab.multitenantERP.repositories.WorkingDayRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkingDayService {
    WorkingDayRepository workingDayRepository;
    WorkingDayService(WorkingDayRepository workingDayRepository){
        this.workingDayRepository = workingDayRepository;
    }

    public List<WorkingDay> getWorkingDays(){
        return workingDayRepository.findAll();
    }

    public List<WorkingDay> updateWorkingDays(List<WorkingDay> workingDays){
        workingDayRepository.flush();
        return workingDayRepository.saveAll(workingDays);
    }

    @PostConstruct
    public void createDefaultUser() {
        List<WorkingDay> workingDayList = new ArrayList<>();
        CompanyContextHolder.setCompanySchema("public");
        if (workingDayRepository.count() == 0) {
            for (Day day : Day.values()) {
                WorkingDay workingDay = new WorkingDay();
                workingDay.setDay(day);
                workingDay.setWorkingDay(true);
                workingDayList.add(workingDay);
            }


            workingDayRepository.saveAll(workingDayList);
        }
    }

}
