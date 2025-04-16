package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.entity.*;
import com.yab.multitenantERP.services.AllowanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/allowance")
public class AllowanceController {

    private final AllowanceService allowanceService;

    public AllowanceController(AllowanceService allowanceService) {
        this.allowanceService = allowanceService;
    }

    // ✅ Add Allowance to Employee
    @PostMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeAllowance>> addAllowanceToEmployee(
            @PathVariable Long employeeId,
            @RequestBody List<EmployeeAllowance> allowances
    ) {
        try {
            List<EmployeeAllowance> result = allowanceService.addAllowancesToEmployee(employeeId, allowances);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ✅ Add Allowance to Position
    @PostMapping("/position/{positionId}")
    public ResponseEntity<List<PositionAllowance>> addAllowanceToPosition(
            @PathVariable Long positionId,
            @RequestBody List<PositionAllowance> allowances
    ) {
        try {
            List<PositionAllowance> result = allowanceService.addAllowanceToPosition(positionId, allowances);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ✅ Create Allowance (type/category)
    @PostMapping
    public ResponseEntity<Allowance> addAllowance(@RequestBody Allowance allowance) {
        try {
            Allowance newAllowance = allowanceService.createAllowance(allowance);
            return ResponseEntity.ok(newAllowance);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/position/{positionId}")
    public ResponseEntity<List<Allowance>> getMissingAllowances(@PathVariable Long positionId) {
        List<Allowance> missingAllowances = allowanceService.getAllowancesNotAssignedToPosition(positionId);
        return ResponseEntity.ok(missingAllowances);
    }

    // ✅ Get single Allowance
    @GetMapping("/{id}")
    public ResponseEntity<Allowance> getAllowance(@PathVariable Long id) {
        Optional<Allowance> allowance = allowanceService.getAllowanceById(id);
        return allowance.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    // ✅ Get Employee's Allowances
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeAllowance>> getEmployeeAllowances(@PathVariable Long employeeId) {
        try {
            return ResponseEntity.ok(allowanceService.getEmployeeAllowances(employeeId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ✅ Get Position's Allowances
    @GetMapping("/position-allowances/{positionId}")
    public ResponseEntity<List<PositionAllowance>> getPositionAllowances(@PathVariable Long positionId) {
        try {
            return ResponseEntity.ok(allowanceService.getPositionAllowances(positionId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ✅ Get all Allowances (types)
    @GetMapping
    public List<Allowance> getAllowances() {
        return allowanceService.getAllAllowances();
    }

    @PutMapping("/{id}")
    public Allowance updateAllowance (@PathVariable Long id, @RequestBody Allowance allowance){
        return allowanceService.updateAllowance(id,allowance);
    }
}
