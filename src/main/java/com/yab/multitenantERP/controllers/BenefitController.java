package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.entity.*;
import com.yab.multitenantERP.entity.Benefit;
import com.yab.multitenantERP.repositories.BenefitRepository;
import com.yab.multitenantERP.services.BenefitService;
import com.yab.multitenantERP.services.BenefitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/benefit")
public class BenefitController {

    private final BenefitService benefitService;

    public BenefitController(BenefitService benefitService) {
        this.benefitService = benefitService;
    }

    @PostMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeBenefit>> addAllowanceToEmployee(
            @PathVariable Long employeeId,
            @RequestBody List<EmployeeBenefit> benefits
    ) {
        try {
            List<EmployeeBenefit> result = benefitService.addBenefitToEmployee(employeeId, benefits);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PostMapping("/position/{positionId}")
    public ResponseEntity<List<PositionBenefit>> addBenefitsToPosition(@RequestBody List<PositionBenefit> benefits,
                                                          @PathVariable Long positionId) {
        try {
            List<PositionBenefit> updatedPosition = benefitService.addBenefitToPosition(positionId, benefits);
            return ResponseEntity.ok(updatedPosition);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @PostMapping
    public ResponseEntity<Benefit> addBenefit(
            @RequestBody Benefit benefit
    ) {
        try {
            Benefit newBenefit = benefitService.createBenefit(benefit);
            return ResponseEntity.ok(newBenefit);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Benefit> getBenefit(@PathVariable Long id) {
        Optional<Benefit> Benefit = benefitService.getBenefitById(id);
        if(Benefit.isPresent()){
            return ResponseEntity.ok(benefitService.getBenefitById(id).get());
        }
        return ResponseEntity.badRequest().body(null);
    }
    @GetMapping("/employee/{id}")
    public List<Benefit> getBenefitByEmployeeId(@PathVariable Long employeeId) {

        return benefitService.getBenefitByEmployeeId(employeeId);

    }

    @GetMapping("/position/{id}")
    public List<Benefit> getBenefitByPositionId(@PathVariable Long positionId) {

        return benefitService.getBenefitByPositionId(positionId);

    }
    @GetMapping
    public List<Benefit> getBenefits(){
        return benefitService.getAllBenefits();
    }

    @PutMapping("/{id}")
    public Benefit updateBenefit(@PathVariable Long id, @RequestBody Benefit benefit) {

        return benefitService.updateBenefit(id,benefit);
    }
}
