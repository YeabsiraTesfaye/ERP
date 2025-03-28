package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.entity.WorkingDay;
import com.yab.multitenantERP.services.WorkingDayService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workingDay")
public class WorkingDayController {
    WorkingDayService workingDayService;
    WorkingDayController(WorkingDayService workingDayService){
        this.workingDayService = workingDayService;
    }

    @GetMapping
    public List<WorkingDay> getAllDays(){
        return workingDayService.getWorkingDays();
    }

    @PutMapping
    public List<WorkingDay> updateWorkingDays(@RequestBody List<WorkingDay> workingDayList){
        return  workingDayService.updateWorkingDays(workingDayList);
    }
}
