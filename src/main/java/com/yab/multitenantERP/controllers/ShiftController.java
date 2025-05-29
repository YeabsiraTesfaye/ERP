package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.entity.Employee;
import com.yab.multitenantERP.entity.Position;
import com.yab.multitenantERP.entity.Shift;
import com.yab.multitenantERP.services.ShiftService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shift")
public class ShiftController {
    ShiftService shiftService;
    ShiftController(ShiftService shiftService){
        this.shiftService = shiftService;
    }
    @PostMapping
    public Shift createShift(@RequestBody Shift shift){
        return shiftService.createShift(shift);
    }

    @PutMapping("/{shiftId}")
    public Shift updateShift(@PathVariable Long shiftId, @RequestBody Shift shift){
        return shiftService.updateShift(shiftId, shift);
    }


    @GetMapping("/{shiftId}")
    public Shift getShift(@PathVariable Long shiftId){
        return shiftService.getShift(shiftId);
    }

    @GetMapping("/setDefault/{shiftId}")
    public Shift setDefaultShift(@PathVariable Long shiftId){
        return shiftService.setDefaultShift(shiftId);
    }

    @GetMapping
    public List<Shift> getShifts(){
        return shiftService.getShifts();
    }

    @PostMapping("/employee/{employeeId}")
    public Employee assignShiftToEmployee(@PathVariable Long employeeId, @RequestBody Long shiftId){
        return shiftService.assignShiftToEmployee(employeeId, shiftId);
    }

    @PostMapping("/position/{positionId}")
    public Position assignShiftToPosition(@PathVariable Long positionId, @RequestBody Long shiftId){
        return shiftService.assignShiftToPosition(positionId, shiftId);
    }
}
