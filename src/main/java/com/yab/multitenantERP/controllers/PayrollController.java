package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.dtos.EmployeePayrollDTO;
import com.yab.multitenantERP.entity.Address;
import com.yab.multitenantERP.entity.PayrollDateRange;
import com.yab.multitenantERP.services.AddressService;
import com.yab.multitenantERP.services.PayrollService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payroll")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @PostMapping("/generate")
    public ResponseEntity<List<EmployeePayrollDTO>> generatePayroll(
            @RequestBody PayrollDateRange dateRange
    ) {
        try {
            List<EmployeePayrollDTO> payroll = payrollService.generatePayroll(dateRange.getStartDate(), dateRange.getEndDate());
            return ResponseEntity.ok(payroll);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
