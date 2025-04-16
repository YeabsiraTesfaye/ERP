package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.entity.IncomeTax;
import com.yab.multitenantERP.services.IncomeTaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/income-tax")
public class IncomeTaxController {

    private final IncomeTaxService incomeTaxService;

    IncomeTaxController(IncomeTaxService incomeTaxService){
        this.incomeTaxService = incomeTaxService;
    }

    // Endpoint to create a new tax rule
    @PostMapping
    public ResponseEntity<IncomeTax> createIncomeTax(@RequestBody IncomeTax incomeTax) {
        IncomeTax createdTax = incomeTaxService.createIncomeTax(incomeTax);
        return ResponseEntity.ok(createdTax);
    }

    // Endpoint to get a tax rule by level
    @GetMapping("/{id}")
    public ResponseEntity<IncomeTax> getIncomeTaxById(@PathVariable Long id) {
        IncomeTax taxRule = incomeTaxService.getIncomeTaxById(id);
        return ResponseEntity.ok(taxRule);
    }

    // Endpoint to update an existing tax rule by id
    @PutMapping("/{id}")
    public ResponseEntity<IncomeTax> updateIncomeTax(@PathVariable Long id, @RequestBody IncomeTax incomeTax) {
        IncomeTax updatedTax = incomeTaxService.updateIncomeTax(id, incomeTax);
        return ResponseEntity.ok(updatedTax);
    }

    // Endpoint to calculate tax based on income and level
    @GetMapping("/calculate/{level}")
    public ResponseEntity<BigDecimal> calculateTax(@PathVariable String level, @RequestParam BigDecimal income) {
        BigDecimal taxAmount = incomeTaxService.calculateTax(income, level);
        return ResponseEntity.ok(taxAmount);
    }


    // New endpoint to get all income taxes
    @GetMapping
    public ResponseEntity<List<IncomeTax>> getAllIncomeTaxes() {
        List<IncomeTax> incomeTaxes = incomeTaxService.getAllIncomeTaxes();
        return ResponseEntity.ok(incomeTaxes);
    }

    @GetMapping("/bySalary/{salary}")
    public ResponseEntity<IncomeTax> bySalary(@PathVariable BigDecimal salary) {
        IncomeTax incomeTax = incomeTaxService.getIncomeTaxBySalary(salary);
        return ResponseEntity.ok(incomeTax);
    }
}