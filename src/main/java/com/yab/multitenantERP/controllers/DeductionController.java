package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.entity.Allowance;
import com.yab.multitenantERP.entity.Deduction;
import com.yab.multitenantERP.entity.EmployeeAllowance;
import com.yab.multitenantERP.entity.PositionAllowance;
import com.yab.multitenantERP.services.AllowanceService;
import com.yab.multitenantERP.services.DeductionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/deduction")
public class DeductionController {

    private final DeductionService deductionService;

    public DeductionController(DeductionService deductionService) {
        this.deductionService = deductionService;
    }

    // ✅ Add Allowance to Employee
    @PostMapping("/{employeeId}")
    public ResponseEntity<List<Deduction>> addAllowanceToEmployee(
            @PathVariable Long employeeId,
            @RequestBody List<Deduction> deductions
    ) {
        try {
            List<Deduction> result = deductionService.addDeductionToEmployee(employeeId, deductions);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ✅ Get single Allowance
    @GetMapping("/{id}")
    public ResponseEntity<Deduction> getDeduction(@PathVariable Long id) {
        Optional<Deduction> allowance = deductionService.getDeductionById(id);
        return allowance.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    // ✅ Get Employee's Allowances
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Deduction>> getEmployeeAllowances(@PathVariable Long employeeId) {
        try {
            return ResponseEntity.ok(deductionService.getEmployeeDeductions(employeeId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    // ✅ Get all Allowances (types)
    @GetMapping
    public List<Deduction> getAllowances() {
        return deductionService.getAllDeductions();
    }

    @PutMapping("/{id}")
    public Deduction updateAllowance (@PathVariable Long id, @RequestBody Deduction deduction){
        return deductionService.updateDeduction(id,deduction);
    }
}
